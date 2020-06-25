package by.jwd.testsys.logic.impl;

import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.TestResultDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.exception.TestLogServiceException;

import java.util.HashSet;
import java.util.Set;


public class TestLogServiceImpl implements TestLogService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();

    @Override
    public void writeUserAnswer(int questionLogId, String[] answers) throws TestLogServiceException {
        Set<Integer> answerSet = new HashSet<>();
        for (String answer : answers) {
            answerSet.add(Integer.parseInt(answer));
        }
        try {
            testLogDAO.writeAnswerLog(questionLogId, answerSet);
        } catch (DAOException e) {
            throw new TestLogServiceException("Exception in TestLogServiceImpl method writeUserAnswer()." +
                    "Impossible to save answer to DB", e);
        }

    }

    @Override
    public int writeQuestionLog(int questionId, int assignmentId) throws TestLogServiceException {
        try {
            return testLogDAO.writeQuestionLog(questionId, assignmentId);
        } catch (DAOException e) {
            throw new TestLogServiceException("Exception in TestLogServiceImpl method writeQuestionLog()." +
                    " Impossible to save question to DB", e);
        }
    }

}
