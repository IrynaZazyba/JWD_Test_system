package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.sender.SslSender;
import by.jwd.testsys.logic.validator.TestValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class TestServiceImpl implements TestService {

    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT = "BeeTesting test assignment";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST = "you have been assigned to the test ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY = "Key: ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE = "Deadline: ";
    private final static String EMAIL_ABOUT_TEST_ASSIGNMENT_REGARDS = "Best regards,\n \"Bee testing\" team.";


    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();
    private TestDAO testDAO = daoFactory.getTestDao();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();
    private UserDAO userDAO = daoFactory.getUserDao();
    private QuestionAnswerDAO questionAnswerDAO = daoFactory.getQuestionAnswerDao();

    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private TestValidator testValidator = validatorFactory.getTestValidator();


    @Override
    public List<Type> allTestsType() throws ServiceException {
        List<Type> testsType;
        try {
            testsType = typeDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Error in TestService allTestsType() method", e);
        }
        return testsType;
    }

    @Override
    public List<Type> typeWithTests(int userId) throws ServiceException {
        List<Type> testsType;

        try {
            testsType = typeDAO.getAll();

            for(Type type:testsType){
                Set<Test> tests = testDAO.getTests(type.getId(), false);
                type.setTests(tests);
            }

            for (Type testType : testsType) {
                Set<Test> tests = testType.getTests();
                for (Test test : tests) {
                    int testId = test.getId();
                    Assignment userAssignment = userDAO.getUserAssignmentByTestId(userId, testId);
                    if (userAssignment != null) {
                        Result testResult = testResultDAO.getTestResult(userAssignment);
                        test.setStarted(testResult != null);
                    } else {
                        test.setStarted(false);
                    }
                }
            }

        } catch (DAOException e) {
            throw new ServiceException("Error in TestService typeWithTests() method", e);
        }
        return testsType;
    }

    @Override
    public Set<Test> getUserAssignmentTests(int userId) throws ServiceException {
        Set<Test> assignmentTest = null;
        try {
            assignmentTest = testDAO.getAssignedTests(userId);

        } catch (DAOException e) {
            throw new ServiceException("Error in TestService typeWithTests() method", e);
        }
        return assignmentTest;
    }

    @Override
    //todo assign название
    public Question getQuestionByTestId(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {
        Question question;
        try {
            int testId = assignment.getTest().getId();
            question = questionAnswerDAO.getQuestionByTestId(testId, assignment.getId());

            if (question != null) {
                checkTestDuration(assignment);
                Set<Answer> answersByQuestionId = questionAnswerDAO.getAnswersByQuestionId(question.getId());
                question.setAnswers(answersByQuestionId);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

        return question;
    }

    @Override
    public Test getTestInfo(int id) throws TestServiceException {
        Test test;
        try {
            test = testDAO.getTestInfo(id);
            int countQuestion = questionAnswerDAO.getCountQuestion(test.getId());
            test.setId(id);
            test.setCountQuestion(countQuestion);

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return test;
    }

    @Override
    public Assignment receiveTestAssignment(int testId, int userId) throws TestServiceException {

        Assignment assignment;
        try {
            assignment = checkAssignment(userId, testId);

            String testKey = testDAO.getTestKey(testId);
            Result result;

            if (assignment == null && testKey == null) {
                Test test = new Test();
                test.setId(testId);
                User user = new User.Builder().withId(userId).build();
                assignment = new Assignment.Builder()
                        .withUser(user)
                        .withAssignmentDate(LocalDate.now())
                        .withDeadline(LocalDate.now().plusDays(7))             //todo deadline
                        .withTest(test)
                        .build();
                Integer integer = userDAO.writeAssignment(assignment);
                assignment.setId(integer);
                result = createResult(assignment);
                writeResult(result);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

        return assignment;
    }

    @Override
    public void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidUserDataException, InvalidTestKeyException {

        String validationResult = null;

        if (key != null) {
            validationResult = testValidator.validate(key);
            if (validationResult != null) {
                throw new InvalidTestKeyException("Invalid user data.", validationResult);
            }
        }


        Assignment assignment = null;
        try {
            assignment = checkAssignment(userId, testId);
            String testKey = testDAO.getTestKey(testId);
            Result result = null;


            if (testKey != null && assignment == null) {
                throw new InvalidUserDataException("Impossible user operation");
            }

            if (testKey != null && assignment != null && getResult(assignment) == null) {

                if (!checkKey(key, testId)) {
                    logger.log(Level.ERROR, "Invalid key");
                    throw new InvalidTestKeyException("Invalid key");
                }

                //todo builder
                result = createResult(assignment);
                writeResult(result);
            }

            if (testKey == null && assignment != null && getResult(assignment) == null) {

                //todo builder
                result = createResult(assignment);
                writeResult(result);
            }


        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    @Override
    public boolean checkKey(String key, int testId) throws TestServiceException {
        boolean isValid = false;
        try {
            String testKey = testDAO.getTestKey(testId);
            if (testKey.equals(key)) {
                isValid = true;
            }
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return isValid;
    }

    private Assignment checkAssignment(int userId, int testId) throws DAOException {
        return userDAO.getUserAssignmentByTestId(userId, testId);
    }


    @Override
    public long calculateTestDuration(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {

        long testDurationSeconds;
        try {
            LocalTime testDuration = testDAO.getTestDuration(assignment.getId());
            Result result = getResult(assignment);
            LocalDateTime dateStart = result.getDateStart();
            Duration duration = Duration.between(dateStart, LocalDateTime.now());

            testDurationSeconds = testDuration.toSecondOfDay() - duration.toSeconds();

            if (testDurationSeconds <= 0) {
                throw new TimeIsOverServiceException("Time is over");
            }

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

        return testDurationSeconds;
    }

    @Override
    public void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException {
        try {
            userDAO.updateAssignment(assignment.getId(), true);

            Result result = calculateResult(assignment);
            result.setDateEnd(localDateTime);
            updateResult(result);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

    }


    @Override
    public LocalDateTime getStartTestTime(int assignmentId) throws TestServiceException {
        LocalDateTime localDateTime;
        try {
            Timestamp testStartDateTime = testResultDAO.getTestStartDateTime(assignmentId);
            localDateTime = testStartDateTime.toLocalDateTime();

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return localDateTime;
    }


    //todo
    @Override
    public LocalTime getTestDuration(int assignmentId) throws TestServiceException {
        LocalTime duration;
        try {
            duration = testDAO.getTestDuration(assignmentId);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return duration;
    }

    private void checkTestDuration(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {

        int assignmentId = assignment.getId();
        LocalTime testDuration = getTestDuration(assignmentId);
        LocalDateTime startTestTime = getStartTestTime(assignmentId);
        Duration duration = Duration.between(startTestTime, LocalDateTime.now());

        if (duration.toSeconds() >= testDuration.toSecondOfDay()) {
            throw new TimeIsOverServiceException("Time is over");
        }

    }

    @Override
    public Assignment getAssignment(int assignmentId) throws TestServiceException {
        Assignment testAssignment = null;
        try {
            testAssignment = userDAO.getUserAssignmentByAssignmentId(assignmentId);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return testAssignment;

    }

    @Override
    public Result getResultInfo(Assignment assignment, Test test) throws TestServiceException {

        Result result = null;
        try {
            result = getResult(assignment);
        } catch (TestServiceException e) {
            throw new TestServiceException("DB problem", e);
        }
        return result;
    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException {
        try {
            return testResultDAO.getUserTestStatistic(userId);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
    }


    private Result calculateResult(Assignment assignment) throws TestServiceException {
        Result result;
        try {
            TestLog testLogByAssignmentId = testLogDAO.getTestLog(assignment.getId());

            Map<Integer, List<Integer>> rightAnswersToQuestionByTestId = questionAnswerDAO.
                    getRightAnswersToQuestionByTestId(assignment.getTest().getId());

            Map<Integer, List<Integer>> userAnswerMap = testLogByAssignmentId.getQuestionAnswerMap();

            int countRight = 0;

            for (Map.Entry<Integer, List<Integer>> entry : userAnswerMap.entrySet()) {
                Integer questionId = entry.getKey();
                List<Integer> value = entry.getValue();
                Collections.sort(value);

                for (Map.Entry<Integer, List<Integer>> rightEntry : rightAnswersToQuestionByTestId.entrySet()) {
                    List<Integer> rightAnswersList = rightEntry.getValue();
                    Collections.sort(rightAnswersList);
                    if (rightEntry.getKey().equals(questionId) && value.equals(rightAnswersList)) {
                        countRight++;
                        break;
                    }
                }
            }
            result = testResultDAO.getTestResult(assignment);
            result.setRightCountQuestion(countRight);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return result;
    }

    @Override
    public Result checkResult(int userId, int testId) throws TestServiceException {

        Result testResult = null;
        try {
            Assignment userAssignmentByTestId = userDAO.getUserAssignmentByTestId(userId, testId);
            if (userAssignmentByTestId != null) {
                testResult = testResultDAO.getTestResult(userAssignmentByTestId);
            }
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return testResult;
    }

    @Override
    public Set<Test> getNotEditedTestByTypeId(int typeId) throws TestServiceException {
        Set<Test> tests = null;
        try {
            tests = testDAO.getTests(typeId, false);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return tests;
    }


    @Override
    public Set<Test> getAllTestByTypeId(int typeId, int currentPage) throws TestServiceException {

        int recordsPerPage = 11;
        int start = currentPage * recordsPerPage - recordsPerPage;
        Set<Test> tests = null;
        try {
            tests = testDAO.getTestsByLimit(typeId, start, recordsPerPage);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return tests;
    }

    //todo Зачем

    private Result getResult(Assignment assignment) throws TestServiceException {
        try {
            return testResultDAO.getTestResult(assignment);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
    }

    private void writeResult(Result result) throws TestServiceException {
        try {
            testResultDAO.insertResult(result);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    private void updateResult(Result result) throws DAOException {
        testResultDAO.updateResult(result);
    }

    private Result createResult(Assignment assignment) throws TestServiceException {

        //todo builder
        int testId = assignment.getTest().getId();

        Result result = new Result.Builder().withDateStart(LocalDateTime.now()).withAssignment(assignment).build();
        int countQuestion = 0;
        try {
            countQuestion = questionAnswerDAO.getCountQuestion(testId);
            result.setCountTestQuestion(countQuestion);

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

        return result;
    }


    //todo передавать Map из контроллера и здесь уже заполнять ответами
    @Override
    public Map<String, Set<User>> assignTestToUsers(int testId, LocalDate deadline, String[] assignUsersId) throws ServiceException, DateOutOfRangeException {

        List<Integer> usersId = new ArrayList<>();
        Map<String, Set<User>> assignmentResult = new HashMap<>();
        Set<User> existsAssignment = new HashSet<>();
        Set<User> successAssignment = new HashSet<>();

        if (!isWithinRange(deadline)) {
            throw new DateOutOfRangeException("Deadline date isn't valid");
        }

        try {
            for (String id : assignUsersId) {
                int userId = Integer.parseInt(id);
                Assignment assignment = checkAssignment(userId, testId);
                if (assignment == null) {
                    usersId.add(userId);
                    User user = userDAO.getUserById(userId);
                    successAssignment.add(user);
                } else {
                    User userById = userDAO.getUserById(userId);
                    existsAssignment.add(userById);
                }
            }

            assignmentResult.put("successAssignment", successAssignment);
            assignmentResult.put("existsAssignment", existsAssignment);
            userDAO.insertAssignment(LocalDate.now(), deadline, testId, usersId);
            sendTestKeyToUsers(successAssignment, testId, deadline);
        } catch (DAOException e) {
            throw new TestServiceException("Error in assignTestToUsers().", e);
        }

        return assignmentResult;
    }

    private void sendTestKeyToUsers(Set<User> assignedUsers, int testId, LocalDate deadline) {

        try {
            Test testInfo = testDAO.getTestInfo(testId);
            SslSender sender = SslSender.getInstance();

            for (User user : assignedUsers) {
                String message = buildEmailMessage(user.getFirstName(), testInfo.getTitle(), testInfo.getKey(), deadline);
                sender.send(EMAIL_ABOUT_TEST_ASSIGNMENT_SUBJECT, message, user.getEmail());
            }
        } catch (DAOSqlException e) {
            //todo
            e.printStackTrace();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailMessage(String userName, String testName, String key, LocalDate deadline) {
        StringBuilder message = new StringBuilder();
        message.append(userName)
                .append(", ")
                .append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_TEST)
                .append("\"").append(testName).append("\"").append(".\n");
        message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_DEADLINE).append(deadline).append(".\n");
        if (key != null) {
            message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_TEXT_KEY).append(key).append(".\n");
        }
        message.append(EMAIL_ABOUT_TEST_ASSIGNMENT_REGARDS);
        return message.toString();
    }


    private boolean isWithinRange(LocalDate deadline) {
        return deadline.isAfter(LocalDate.now());
    }

    @Override
    public void deleteAssignment(int assignment_id) throws ServiceException {
        try {
            userDAO.deleteAssignment(assignment_id, LocalDate.now());
        } catch (DAOSqlException e) {
            throw new TestServiceException("DAOSqlException  in deleteAssignment().", e);
        }

    }

    @Override
    public int receiveCountTestPages(int typeId) throws TestServiceException {
        int countPageRows = 11;
        int countTest;
        int numberOfPages;
        try {
            countTest = testDAO.getCountTests(typeId);
            numberOfPages = countTest / countPageRows;
            if (numberOfPages % countPageRows > 0) {
                numberOfPages++;
            }
        } catch (DAOException e) {
            throw new TestServiceException("DAOSqlException  in deleteAssignment().", e);
        }

        return numberOfPages;
    }

}
