package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ImpossibleTestDataServiceException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.exception.TestServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class TestServiceImpl implements TestService {
    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestTypeDAO typeDAO = daoFactory.getTypeDao();
    private TestDAO testDAO = daoFactory.getTestDao();
    private UserDAO userDAO = daoFactory.getUserDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();


    @Override
    public List<Type> allTestsType() throws ServiceException {
        List<Type> testsType = null;
        try {
            testsType = typeDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Error in TestService allTestsType() method", e);
        }
        return testsType;
    }

    @Override
    public Set<Type> typeWithTests() throws ServiceException {
        Set<Type> testsType = null;

        try {
            testsType = typeDAO.getTypeWithTests();

        } catch (DAOException e) {
            throw new ServiceException("Error in TestService typeWithTests() method", e);
        }
        return testsType;
    }


    @Override
    public Question getQuestionByTestId(Assignment assignment) throws TestServiceException, ImpossibleTestDataServiceException {
        Question question = null;
        try {
            int testId = assignment.getTest().getId();
            if (getResult(assignment.getId()) == null) {
                logger.log(Level.ERROR, "Incorrect data from client side");
                throw new ImpossibleTestDataServiceException("Problem with params from client side");
            }

            question = testDAO.getQuestionByTestId(testId, assignment.getId());

            if (question != null) {
                Set<Answer> answersByQuestionId = testDAO.getAnswersByQuestionId(question.getId());
                question.setAnswers(answersByQuestionId);

            } else {
                LocalDateTime testEnded = LocalDateTime.now();
                completeTest(assignment, testEnded);
            }

        } catch (DAOException e) {
            throw new TestServiceException("DB problem", e);
        }

        return question;
    }

    @Override
    public Test getTestInfo(int id) throws TestServiceException {
        Test test = null;
        try {
            Test dbTest = testDAO.getTestInfo(id);
            Set<Question> questions = dbTest.getQuestions();
            int countQuestion = questions.size();
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
    //todo Переименовать
    public Assignment exeTest(int testId, int userId, String key) throws TestServiceException {
        Assignment assignment = checkAssignment(userId, testId);
        if (assignment != null) {

            try {
                checkResult(assignment, key);
            } catch (DAOSqlException e) {
                throw new TestServiceException("DB problem", e);
            }
        }

        return assignment;
    }

    private void checkResult(Assignment assignment, String key) throws TestServiceException, DAOSqlException {
//todo exception
        if (getResult(assignment.getId()) == null
                && key != null
                && checkKey(Integer.parseInt(key), assignment.getTest().getId())) {

            Result testResult = testDAO.getTestResultByAssignmentId(assignment.getId());
            if (testResult == null) {
                testResult = new Result();
                testResult.setDateStart(LocalDateTime.now());
                testResult.setAssignment(assignment);
                testDAO.insertResult(testResult);
            }
        }
    }

    @Override
    public Result getResult(int assignmentId) throws TestServiceException {
        try {
            return testDAO.getTestResultByAssignmentId(assignmentId);
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

    private Assignment checkAssignment(int userId, int testId) {
        return userDAO.getUserAssignmentByTestId(userId, testId);
    }


    private void completeTest(Assignment assignment, LocalDateTime localDateTime) throws TestServiceException {
        try {
            testDAO.writeAssignment(assignment.getId(), true);
            Result result = getResult(assignment.getId());
            result = calculateResult(assignment, result, localDateTime);
            writeResultToDB(result);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    private Result calculateResult(Assignment assignment, Result result, LocalDateTime localDateTime) throws TestServiceException {

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
            result.setDateEnd(localDateTime);
            result.setRightCountQuestion(countRight);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
        return result;
    }

    private void writeResultToDB(Result result) throws DAOSqlException {
        testDAO.updateResult(result);
    }

    @Override
    public LocalDateTime getStartTestTime(int assignmentId) {
        LocalDateTime localDateTime = null;
        try {
            Timestamp testStartDateTime = testDAO.getTestStartDateTime(assignmentId);
            localDateTime = testStartDateTime.toLocalDateTime();
            System.out.println("toLD " + localDateTime);
            Duration duration = Duration.between(localDateTime, LocalDateTime.now());

            System.out.println("duration " + duration);
        } catch (DAOSqlException e) {
            e.printStackTrace();
        }
        return localDateTime;
    }

}
