package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestServiceImpl implements TestService {

    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
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
            testsType = testDAO.getTypes();
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService allTestsType() method", e);
        }
        return testsType;
    }

    @Override
    public List<Type> typeWithTests(int userId) throws ServiceException {
        List<Type> testsType;

        try {
            testsType = testDAO.getTypes();

            for (Type type : testsType) {
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
            throw new TestServiceException("DAOException in TestService typeWithTests() method", e);
        }
        return testsType;
    }

    @Override
    public Set<Test> getUserAssignmentTests(int userId) throws ServiceException {
        Set<Test> assignmentTest;
        try {
            assignmentTest = testDAO.getAssignedTests(userId);

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getUserAssignmentTests() method", e);
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
            throw new TestServiceException("DAOException in TestService getQuestionByTestId() method", e);
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
            throw new TestServiceException("DAOException in TestService getTestInfo() method", e);
        }
        return test;
    }

    @Override
    public Assignment checkTestAssignment(int testId, int userId) throws TestServiceException {

        Assignment assignment;
        try {
            assignment = userDAO.getUserAssignmentByTestId(userId, testId);

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
                testResultDAO.insertResult(result);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService checkTestAssignment() method", e);
        }

        return assignment;
    }

    @Override
    public void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidUserDataException, InvalidTestKeyException {

        String validationResult;

        if (key != null) {
            validationResult = testValidator.validate(key);
            if (validationResult != null) {
                throw new InvalidTestKeyException("Invalid user data.", validationResult);
            }
        }


        Assignment assignment;
        try {
            assignment = userDAO.getUserAssignmentByTestId(userId, testId);
            String testKey = testDAO.getTestKey(testId);
            Result result;


            if (testKey != null && assignment == null) {
                throw new InvalidUserDataException("Impossible user operation");
            }


            result = testResultDAO.getTestResult(assignment);

            if (testKey != null && assignment != null && result == null) {

                if (!checkKey(key, testId)) {
                    logger.log(Level.ERROR, "Invalid key");
                    throw new InvalidTestKeyException("Invalid key");
                }

                //todo builder
                result = createResult(assignment);
                testResultDAO.insertResult(result);
            }

            if (testKey == null && assignment != null && result == null) {

                //todo builder
                result = createResult(assignment);
                testResultDAO.insertResult(result);
            }


        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService checkPermission() method", e);
        }

    }

    @Override
    public Set<Result> receiveResultData(int typeId, int testId, int userId, LocalDate date) throws TestLogServiceException {
        Set<Result> result;

        try {
            result=testResultDAO.getTestResult(typeId,testId,userId,date);
        } catch (DAOException e) {
            throw new TestLogServiceException("DAOException in TestLogServiceImpl method receiveResultData()."+
                    "Impossible to receive result data", e);
        }
        return result;
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
            throw new TestServiceException("DAOException in TestService checkKey() method", e);
        }
        return isValid;
    }


    @Override
    public long calculateTestDuration(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {

        long testDurationSeconds;
        try {
            LocalTime testDuration = testDAO.getTestDuration(assignment.getId());
            Result result = testResultDAO.getTestResult(assignment);
            LocalDateTime dateStart = result.getDateStart();
            Duration duration = Duration.between(dateStart, LocalDateTime.now());

            testDurationSeconds = testDuration.toSecondOfDay() - duration.toSeconds();

            if (testDurationSeconds <= 0) {
                throw new TimeIsOverServiceException("Time is over");
            }

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService calculateTestDuration() method", e);
        }

        return testDurationSeconds;
    }

    @Override
    public void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException {
        try {
            userDAO.updateAssignment(assignment.getId(), true);

            Result result = calculateResult(assignment);
            result.setDateEnd(localDateTime);
            testResultDAO.updateResult(result);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService completeTest() method", e);
        }

    }


    @Override
    public LocalDateTime getStartTestTime(int assignmentId) throws TestServiceException {
        LocalDateTime localDateTime;
        try {
            Timestamp testStartDateTime = testResultDAO.getTestStartDateTime(assignmentId);
            localDateTime = testStartDateTime.toLocalDateTime();

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getStartTestTime() method", e);
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
            throw new TestServiceException("DAOException in TestService getTestDuration() method", e);
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
    public Result getResultInfo(Assignment assignment, Test test) throws TestServiceException {

        Result result;
        try {
            result = testResultDAO.getTestResult(assignment);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getResultInfo() method", e);
        }
        return result;
    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException {
        try {
            return testResultDAO.getUserTestStatistic(userId);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getUserTestStatistic() method", e);
        }
    }


    @Override
    public Assignment getAssignment(int assignmentId) throws TestServiceException {
        Assignment testAssignment;
        try {
            testAssignment = userDAO.getUserAssignmentByAssignmentId(assignmentId);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getAssignment() method", e);
        }
        return testAssignment;

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
            throw new TestServiceException("DAOException in TestService calculateResult() method", e);
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
            throw new TestServiceException("DAOException in TestService checkResult() method", e);
        }
        return testResult;
    }

    @Override
    public Set<Test> getNotEditedTestByTypeId(int typeId) throws TestServiceException {
        Set<Test> tests;
        try {
            tests = testDAO.getTests(typeId, false);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getNotEditedTestByTypeId() method", e);
        }
        return tests;
    }


    @Override
    public Set<Test> getAllTestByTypeId(int typeId, int currentPage) throws TestServiceException {

        int recordsPerPage = 11;
        int start = currentPage * recordsPerPage - recordsPerPage;
        Set<Test> tests;
        try {
            tests = testDAO.getTestsByLimit(typeId, start, recordsPerPage);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getAllTestByTypeId() method", e);
        }
        return tests;
    }


    private Result createResult(Assignment assignment) throws TestServiceException {

        //todo builder
        int testId = assignment.getTest().getId();

        Result result = new Result.Builder().withDateStart(LocalDateTime.now()).withAssignment(assignment).build();
        int countQuestion;
        try {
            countQuestion = questionAnswerDAO.getCountQuestion(testId);
            result.setCountTestQuestion(countQuestion);

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService createResult() method", e);
        }

        return result;
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
            throw new TestServiceException("DAOException in TestService receiveCountTestPages() method", e);
        }

        return numberOfPages;
    }

}
