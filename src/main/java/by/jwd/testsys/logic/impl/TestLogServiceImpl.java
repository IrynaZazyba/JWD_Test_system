package by.jwd.testsys.logic.impl;

import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.validator.FrontDataValidator;
import by.jwd.testsys.logic.validator.factory.ValidatorFactory;

import java.util.HashSet;
import java.util.Set;


public class TestLogServiceImpl implements TestLogService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestLogDAO testLogDAO = daoFactory.getTestLogDao();

    private ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
    private FrontDataValidator frontDataValidator = validatorFactory.getFrontDataValidator();


    /**
     * Saving user answers to the question in log db table
     *
     * @param questionLogId ID of the question in log db table
     *                      to which the answers refer
     * @param answers       users answers to question
     * @throws TestLogServiceException  in case of an error while saving to the database
     * @throws InvalidUserDataException in case of invalid questionLogId, answers
     */
    @Override
    public void writeUserAnswer(int questionLogId, String[] answers) throws TestLogServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(questionLogId)) {
            throw new InvalidUserDataException("Invalid userId in TestLogServiceImpl writeUserAnswer() method");
        }

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

    /**
     * Saving question shown to the user in log db table
     *
     * @param questionId   ID of the question shown to the user
     * @param assignmentId to which the test question belongs
     * @return id assigned to the record of the question shown to the user
     * in log db table
     * @throws TestLogServiceException  in case of an error while saving to the database
     * @throws InvalidUserDataException in case of invalid questionLogId, answers
     */
    @Override
    public int writeQuestionLog(int questionId, int assignmentId) throws TestLogServiceException, InvalidUserDataException {

        if (!frontDataValidator.validateId(questionId)) {
            throw new InvalidUserDataException("Invalid userId in TestLogServiceImpl writeQuestionLog() method");
        }

        try {
            return testLogDAO.writeQuestionLog(questionId, assignmentId);
        } catch (DAOException e) {
            throw new TestLogServiceException("Exception in TestLogServiceImpl method writeQuestionLog()." +
                    " Impossible to save question to DB", e);
        }
    }

}
