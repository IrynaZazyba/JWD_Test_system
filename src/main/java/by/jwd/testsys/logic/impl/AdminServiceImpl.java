package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Answer;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidDeleteActionServiceException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AdminServiceImpl implements AdminService {

    private final DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private TestDAO testDAO = daoFactory.getTestDao();


    public Set<Question> getAllQuestionByTestId(int testId) {

        Set<Question> testQuestions = null;

        return testQuestions;
    }

    @Override
    public void deleteTest(int testId) throws AdminServiceException, InvalidDeleteActionServiceException {
        try {
            int countIncompleteTestAssignment = testDAO.getCountIncompleteTestAssignment(testId);
            if (countIncompleteTestAssignment != 0) {
                throw new InvalidDeleteActionServiceException("Impossible to delete current test. Assignment exists.");
            }

            testDAO.deleteTestById(testId, LocalDateTime.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }

    @Override
    public int createTest(int typeId, String title, String key, int duration) throws AdminServiceException {
        int generatedTestId;
        LocalTime testDuration = buildLocalTimeFromMinuteDuration(duration);
        Test test = new Test(title, key, testDuration, true);
        try {
            generatedTestId = testDAO.saveTest(test, typeId);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
        return generatedTestId;
    }

    @Override
    public void updateTestData(int testId, int typeId, String title, String key, int duration) throws AdminServiceException {
        LocalTime testDuration = buildLocalTimeFromMinuteDuration(duration);
        Test test = new Test(testId, title, key, testDuration, true);
        try {
            testDAO.updateTest(test, typeId);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }

    @Override
    public void createQuestionAnswer(String question, Map<Integer, String> answers, List<Integer> rightAnswers, int testId) throws AdminServiceException {

        Question createdQuestion = new Question(question);
        Set<Answer> createdAnswers = new HashSet<>();
        try {

            answers.forEach((k, v) -> {
                Answer answer = new Answer(v);
                answer.setResult(false);
                rightAnswers.forEach(rightAnswer ->
                {
                    if (k.equals(rightAnswer)) {
                        answer.setResult(true);
                    }
                });
                createdAnswers.add(answer);
            });
            createdQuestion.setAnswers(createdAnswers);

            testDAO.saveQuestionWithAnswers(createdQuestion,testId);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }


    }

    private LocalTime buildLocalTimeFromMinuteDuration(int duration) {
        LocalTime testDuration = null;

        if (duration > 0) {
            testDuration = LocalTime.ofSecondOfDay(duration * 60);
        }
        return testDuration;
    }

}
