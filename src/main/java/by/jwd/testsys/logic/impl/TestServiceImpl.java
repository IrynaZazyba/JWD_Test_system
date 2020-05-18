package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
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
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();
    private TestDAO testDAO = daoFactory.getTestDao();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();
    private UserDAO userDAO = daoFactory.getUserDao();
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
    public Set<Type> typeWithTests(int userId) throws ServiceException {
        Set<Type> testsType;

        try {
            testsType = typeDAO.getTypeWithTests();

            for (Type testType : testsType) {
                Set<Test> tests = testType.getTests();
                for (Test test : tests) {
                    int testId = test.getId();
                    Assignment userAssignment = userDAO.getUserAssignmentByTestId(userId, testId);
                    if (userAssignment != null) {
                        Result testResult = testResultDAO.getTestResult(userAssignment);
                        test.setStarted(testResult != null);
                        test.setFlag(1);
                    } else {
                        test.setStarted(false);
                        test.setFlag(0);

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
            assignmentTest = testDAO.getAssignmentTest(userId);
        } catch (DAOSqlException e) {
            throw new ServiceException("Error in TestService typeWithTests() method", e);
        }
        return assignmentTest;
    }

    @Override
    //todo assign
    public Question getQuestionByTestId(Assignment assignment) throws TestServiceException, TimeIsOverServiceException {
        Question question;
        try {
            int testId = assignment.getTest().getId();
            question = testDAO.getQuestionByTestId(testId, assignment.getId());

            if (question != null) {
                checkTestDuration(assignment);
                Set<Answer> answersByQuestionId = testDAO.getAnswersByQuestionId(question.getId());
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
            Test dbTest = testDAO.getTestInfo(id);
            int countQuestion = testDAO.getCountQuestion(dbTest.getId());
            String title = dbTest.getTitle();
            LocalTime duration = dbTest.getDuration();
            String key = dbTest.getKey();

            test = new Test(id, title, countQuestion, key, duration);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return test;
    }

    @Override
    public Assignment receiveTestAssignment(int testId, int userId) throws TestServiceException {

        Assignment assignment = null;
        try {
            assignment = checkAssignment(userId, testId);

            String testKey = testDAO.getTestKey(testId);
            Result result = null;

            if (assignment == null && testKey == null) {
                Test test = new Test();
                test.setId(testId);
                User user = new User();
                user.setId(userId);
                assignment = new Assignment(user, LocalDate.now(), LocalDate.now().plusDays(7), test);
                Integer integer = testDAO.writeAssignment(assignment);
                assignment.setId(integer);
                result = createResult(assignment);
                writeResult(result);
            }

        } catch (DAOSqlException e) {
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


        } catch (DAOSqlException e) {
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
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
        return isValid;
    }

    private Assignment checkAssignment(int userId, int testId) throws DAOSqlException {
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

        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

        return testDurationSeconds;
    }

    @Override
    public void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException {
        try {
            testDAO.updateAssignment(assignment.getId(), true);

            Result result = calculateResult(assignment);
            result.setDateEnd(localDateTime);
            updateResult(result);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }


    @Override
    public LocalDateTime getStartTestTime(int assignmentId) throws TestServiceException {
        LocalDateTime localDateTime;
        try {
            Timestamp testStartDateTime = testDAO.getTestStartDateTime(assignmentId);
            localDateTime = testStartDateTime.toLocalDateTime();

        } catch (DAOSqlException e) {
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
        } catch (DAOSqlException e) {
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
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
        return testAssignment;

    }

    @Override
    public double calculatePercentageOfCorrectAnswers(Assignment assignment, Test test) throws TestServiceException {

        Result result = null;
        try {
            result = getResult(assignment);
            int countQuestion = test.getCountQuestion();
            return (result.getRightCountQuestion() * 100) / countQuestion;
        } catch (TestServiceException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException {
        try {
            return testResultDAO.getUserTestStatistic(userId);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
    }


    private Result calculateResult(Assignment assignment) throws TestServiceException {
        Result result = null;
        try {
            TestLog testLogByAssignmentId = testLogDAO.getTestLog(assignment.getId());

            Map<Integer, List<Integer>> rightAnswersToQuestionByTestId = testDAO.
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
            System.out.println(countRight);
            result = testResultDAO.getTestResult(assignment);
            result.setRightCountQuestion(countRight);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
        return result;
    }


    private Result getResult(Assignment assignment) throws TestServiceException {
        try {
            return testResultDAO.getTestResult(assignment);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
    }

    private void writeResult(Result result) throws TestServiceException {
        try {
            testResultDAO.insertResult(result);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    private void updateResult(Result result) throws DAOSqlException {
        testResultDAO.updateResult(result);
    }

    private Result createResult(Assignment assignment) throws TestServiceException {

        //todo builder
        int testId = assignment.getTest().getId();

        Result result = new Result();
        result.setDateStart(LocalDateTime.now());
        result.setAssignment(assignment);
        int countQuestion = 0;
        try {
            countQuestion = testDAO.getCountQuestion(testId);
            result.setCountTestQuestion(countQuestion);

        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

        return result;
    }

}
