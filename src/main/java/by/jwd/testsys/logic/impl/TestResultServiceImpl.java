package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.TestLog;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.TestResultDAO;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestResultServiceImpl implements TestResultService {

    private final static Logger logger = LogManager.getLogger();

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestResultDAO testResultDAO = daoFactory.getTestResultDao();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();
    private TestDAO testDAO = daoFactory.getTestDao();


    @Override
    public Result getResult(Assignment assignment) throws TestServiceException {
        try {
            return testResultDAO.getTestResult(assignment);
        } catch (DAOSqlException e) {
            throw new TestServiceException("DB problem", e);
        }
    }

    @Override
    public Result calculateResult(Assignment assignment, Result result, LocalDateTime localDateTime) throws TestServiceException {

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

    @Override
    public void checkResult(Assignment assignment, String key) throws TestServiceException, DAOSqlException {
//todo exception
        Result result = testResultDAO.getTestResult(assignment);

        //todo норм ли один сервис вызывать в другом?
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();


        if (result == null
                && key != null
                && testService.checkKey(Integer.parseInt(key), assignment.getTest().getId())) {

            Result testResult = testResultDAO.getTestResult(assignment);
            if (testResult == null) {
                testResult = new Result();
                testResult.setDateStart(LocalDateTime.now());
                testResult.setAssignment(assignment);
                testDAO.insertResult(testResult);
            }
        }
    }

    @Override
    public void writeResultToDB(Result result) throws DAOSqlException {
        testDAO.updateResult(result);
    }

    @Override
    public double calculatePercentageOfCorrectAnswers(Result result, Test test) {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        double percentageOfCorrectAnswers;


        int countQuestion = test.getCountQuestion();

        percentageOfCorrectAnswers = (result.getRightCountQuestion()*100)/countQuestion;


        return percentageOfCorrectAnswers;
    }

}
