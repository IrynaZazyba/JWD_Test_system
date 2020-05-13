package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.TestResultDAO;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.exception.TestServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestResultServiceImpl implements TestResultService {

    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();
    private TestDAO testDAO = daoFactory.getTestDao();


    //todo надо ли или из дао тянуть
    @Override
    public Result getResult(Assignment assignment) throws TestServiceException {
        try {
            return testResultDAO.getTestResult(assignment);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
    }

    @Override
    public Result calculateResult(Assignment assignment) throws TestServiceException {
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

    @Override
    public Result createResult(Assignment assignment) throws TestServiceException {

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

    @Override
    public void writeResult(Result result) throws TestServiceException {
        try {
            testResultDAO.insertResult(result);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }

    }

    @Override
    public void updateResult(Result result) throws DAOSqlException {
        testResultDAO.updateResult(result);
    }

    @Override
    public double calculatePercentageOfCorrectAnswers(Result result, Test test) {

        int countQuestion = test.getCountQuestion();
        return (result.getRightCountQuestion() * 100) / countQuestion;

    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws TestServiceException {
        try {
            return testResultDAO.getUserTestStatistic(userId);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
    }


}
