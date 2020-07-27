package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;

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

    private final static int DEFAULT_DEADLINE_VALUE = 7;

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestDAO testDAO = daoFactory.getTestDao();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();
    private UserDAO userDAO = daoFactory.getUserDao();
    private QuestionAnswerDAO questionAnswerDAO = daoFactory.getQuestionAnswerDao();

    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private FrontDataValidator frontDataValidator = validatorFactory.getFrontDataValidator();

    @Override
    public List<Type> allTestsType() throws TestServiceException {
        List<Type> testsType;
        try {
            testsType = testDAO.getTypes();
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService allTestsType() method", e);
        }
        return testsType;
    }

    @Override
    public Set<Test> getUserAssignmentTests(int userId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in TestServiceImpl typeWithTests() method");
        }

        Set<Test> assignmentTest;
        try {
            assignmentTest = testDAO.getAssignedTests(userId);

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getUserAssignmentTests() method", e);
        }
        return assignmentTest;
    }

    @Override
    public Question getQuestion(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {
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
            throw new TestServiceException("DAOException in TestService getQuestion() method", e);
        }

        return question;
    }

    @Override
    public Test getTestInfo(int testId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid userId in TestServiceImpl typeWithTests() method");
        }

        Test test;
        try {
            test = testDAO.getTestInfo(testId);
            test.setId(testId);
            int countQuestion = questionAnswerDAO.getCountQuestion(testId);
            test.setCountQuestion(countQuestion);

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getTestInfo() method", e);
        }
        return test;
    }

    /**
     * Check if user have assignment
     * if the user was assigned to the test will return the existing assignment
     * if test without a key and no assignment, the test will be assigned to the user
     * and the created assignment will be returned
     *
     * @param testId test id
     * @param userId user id
     * @return user assignment
     * @throws TestServiceException     in case of error getting data from the database
     * @throws InvalidUserDataException in case if the parameters passed to the method are not valid
     */
    @Override
    public Assignment checkTestAssignment(int testId, int userId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(testId) ||
                !frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in TestServiceImpl typeWithTests() method");
        }


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
                        .withDeadline(LocalDate.now().plusDays(DEFAULT_DEADLINE_VALUE))
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

    /**
     * Checks if the user has access to take this test:
     * if test key exists and user don't have assignment
     * InvalidUserDataException exception will be thrown;
     * if test key exists and user have assignment is
     * checking if result dont't exists (test isn't started).
     * When the conditions are reached, it is checked whether
     * the entered and the existing key match and a result is
     * created;
     * if test key not exists, assignment exists and result
     * not exists result will be created.
     *
     * @param userId user id
     * @param testId test id
     * @param key    key to test
     * @throws TestServiceException     in case of error getting data from the database
     * @throws InvalidUserDataException in case if the parameters passed to the method are not valid
     * @throws InvalidTestKeyException  in case the key entered by the user is incorrect
     */
    @Override
    public void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidUserDataException, InvalidTestKeyException {

        if (!frontDataValidator.validateId(testId) ||
                !frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in TestServiceImpl typeWithTests() method");
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
                    throw new InvalidTestKeyException("Invalid key");
                }
                result = createResult(assignment);
                testResultDAO.insertResult(result);
            }

            if (testKey == null && assignment != null && result == null) {
                result = createResult(assignment);
                testResultDAO.insertResult(result);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService checkPermission() method", e);
        }

    }

    @Override
    public Set<Result> receiveResultData(int typeId, int testId, int userId, LocalDate date) throws TestLogServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId) ||
                !frontDataValidator.validateId(testId) ||
                !frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid userId in TestServiceImpl receiveResultData() method");
        }

        Set<Result> result;

        try {
            result = testResultDAO.getTestResult(typeId, testId, userId, date);
        } catch (DAOException e) {
            throw new TestLogServiceException("DAOException in TestLogServiceImpl method receiveResultData()." +
                    "Impossible to receive result data", e);
        }
        return result;
    }

    private boolean checkKey(String key, int testId) throws DAOException {

        boolean isValid = false;
        String testKey = testDAO.getTestKey(testId);
        if (testKey.equals(key)) {
            isValid = true;
        }

        return isValid;
    }


    /**
     * The value of the test duration is obtained, the test start time
     * is obtained and the difference is calculated to find the time
     * elapsed from the beginning of the test
     *
     * @param assignment user assignment
     * @return time elapsed since the start of the test
     * @throws TestServiceException       in case of error getting data from the database
     * @throws TimeIsOverServiceException if the time allotted for the test has run out
     */
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
            testResultDAO.updateResultDateEndRightCount(result);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService completeTest() method", e);
        }

    }


    private void checkTestDuration(Assignment assignment) throws TimeIsOverServiceException, DAOException {

        int assignmentId = assignment.getId();
        LocalTime testDuration = testDAO.getTestDuration(assignmentId);

        Timestamp testStartDateTime = testResultDAO.getTestStartDateTime(assignmentId);
        LocalDateTime startTestTime = testStartDateTime.toLocalDateTime();

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
            int countQuestion = questionAnswerDAO.getCountQuestion(test.getId());
            result.setCountTestQuestion(countQuestion);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getResultInfo() method", e);
        }
        return result;
    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId)) {
            throw new InvalidUserDataException("Invalid id");
        }

        try {
            return testResultDAO.getUserTestStatistic(userId);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getUserTestStatistic() method", e);
        }
    }


    @Override
    public Assignment getAssignment(int assignmentId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(assignmentId)) {
            throw new InvalidUserDataException("Invalid assignmentId in TestService getAssignment() method");
        }

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
    public Result checkResult(int userId, int testId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(userId) || !frontDataValidator.validateId(testId)) {
            throw new InvalidUserDataException("Invalid id in TestServiceImpl checkResult() method");
        }

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
    public Set<Test> getNotEditedTestByTypeId(int typeId) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId)) {
            throw new InvalidUserDataException("Invalid id in TestServiceImpl checkResult() method");
        }

        Set<Test> tests;
        try {
            tests = testDAO.getTests(typeId, false);
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getNotEditedTestByTypeId() method", e);
        }
        return tests;
    }


    @Override
    public Set<Test> getTestByTypeId(int typeId, int currentPage, int recordsPerPage) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId) ||
                !frontDataValidator.validatePositiveNumber(currentPage) ||
                !frontDataValidator.validatePositiveNumber(recordsPerPage)) {
            throw new InvalidUserDataException("Invalid assignmentId in TestService getTestByTypeId() method");
        }

        int start = currentPage * recordsPerPage - recordsPerPage;
        Set<Test> tests;
        try {
            tests = testDAO.getTestsByLimit(typeId, start, recordsPerPage);
            for (Test test : tests) {
                if (testDAO.getCountTestAssignment(test.getId(), false) != 0) {
                    test.setStarted(true);
                } else {
                    test.setStarted(false);
                }
            }
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getTestByTypeId() method", e);
        }
        return tests;
    }


    @Override
    public Set<Test> getTestsPermittedForUser(int typeId, int currentPage, int recordsPerPage) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId) ||
                !frontDataValidator.validatePositiveNumber(currentPage) ||
                !frontDataValidator.validatePositiveNumber(recordsPerPage)) {
            throw new InvalidUserDataException("Invalid assignmentId in TestService getTestByTypeId() method");
        }

        int start = currentPage * recordsPerPage - recordsPerPage;
        Set<Test> tests;
        try {
            tests = testDAO.getTestsByLimit(typeId, start, recordsPerPage, false, false);
            for (Test test : tests) {
                int countQuestion = questionAnswerDAO.getCountQuestion(test.getId());
                test.setCountQuestion(countQuestion);
            }
        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService getTestByTypeId() method", e);
        }
        return tests;
    }


    private Result createResult(Assignment assignment) throws TestServiceException {

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
    public int receiveNumberTestPages(int typeId, int recordsPerPage, boolean isEdited, boolean isExistsKey) throws TestServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(typeId) ||
                !frontDataValidator.validatePositiveNumber(recordsPerPage)) {
            throw new InvalidUserDataException("Invalid assignmentId in TestService getTestByTypeId() method");
        }

        int numberOfPages;
        int numberRecords;

        try {
            if (!isEdited) {
                numberRecords = testDAO.getCountTests(typeId, isEdited, isExistsKey);
            } else {
                numberRecords = testDAO.getCountTests(typeId);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DAOException in TestService receiveNumberTestPages() method", e);
        }

        numberOfPages = numberRecords / recordsPerPage;
        if ((numberRecords % recordsPerPage) > 0) {
            numberOfPages++;
        }

        return numberOfPages;
    }

}
