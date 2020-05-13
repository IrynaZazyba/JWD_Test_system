package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.factory.ServiceFactory;
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
    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();
    private TestDAO testDAO = daoFactory.getTestDao();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private UserDAO userDAO = daoFactory.getUserDao();


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
    public Set<Type> typeWithTests() throws ServiceException {
        Set<Type> testsType;

        try {
            testsType = typeDAO.getTypeWithTests();

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
            int key = dbTest.getKey();

            test = new Test(id, title, countQuestion, key, duration);
        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }
        return test;
    }

    @Override
    public Assignment receiveTestAssignment(int testId, int userId) throws TestServiceException, InvalidKeyException {


        //todo validate key
        Assignment assignment = null;
        try {
            assignment = checkAssignment(userId, testId);

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestResultService testResultService = serviceFactory.getTestResultService();

            Integer testKey = testDAO.getTestKey(testId);
            Result result = null;

            if (assignment == null && testKey == 0) {
                Test test = new Test();
                test.setId(testId);
                User user = new User();
                user.setId(userId);
                assignment = new Assignment(user, LocalDate.now(), LocalDate.now().plusDays(7), test);
                Integer integer = testDAO.writeAssignment(assignment);
                assignment.setId(integer);
                result = testResultService.createResult(assignment);
                testResultService.writeResult(result);
            }

        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

        return assignment;
    }

    @Override
    public void checkPermission(int userId, int testId, String key) throws TestServiceException, InvalidKeyException, InvalidUserDataException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestResultService testResultService = serviceFactory.getTestResultService();

        Assignment assignment = null;
        try {
            assignment = checkAssignment(userId, testId);
            Integer testKey = testDAO.getTestKey(testId);
            Result result = null;


            if (testKey != 0 && assignment == null) {
                throw new InvalidUserDataException("Impossible user operation");
            }

            if (testKey != 0 && assignment != null && testResultService.getResult(assignment) == null) {

                if (!checkKey(Integer.parseInt(key), testId)) {
                    logger.log(Level.ERROR, "Invalid key");
                    throw new InvalidKeyException("Invalid key");
                }

                result = testResultService.createResult(assignment);
                testResultService.writeResult(result);
            }


        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    @Override
    public boolean checkKey(Integer key, int testId) throws TestServiceException {
        boolean isValid = false;
        try {
            Integer testKey = testDAO.getTestKey(testId);
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
    public Integer getTestKey(int testId) throws TestServiceException {

        try {
            return testDAO.getTestKey(testId);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    @Override
    public void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException {
        try {
            testDAO.updateAssignment(assignment.getId(), true);

            //todo норм ли один сервис вызывать в другом?
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestResultService testResultService = serviceFactory.getTestResultService();

            Result result = testResultService.calculateResult(assignment);
            result.setDateEnd(localDateTime);
            testResultService.updateResult(result);
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
}
