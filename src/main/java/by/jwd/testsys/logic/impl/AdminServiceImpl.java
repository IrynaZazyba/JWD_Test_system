package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.Answer;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.ExistsTypeAdminServiceException;
import by.jwd.testsys.logic.exception.InvalidDeleteActionServiceException;

import java.time.LocalDate;
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
    public int createTest(int typeId, String title, String key, LocalTime duration) throws AdminServiceException {
        int generatedTestId;
        if (key.equals("")) {
            key = null;
        }
        Test test = new Test(title, key, duration, true);
        try {
            generatedTestId = testDAO.saveTest(test, typeId);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
        return generatedTestId;
    }

    @Override
    public void updateTestData(int testId, int typeId, String title, String key, LocalTime duration) throws AdminServiceException {
        Test test = new Test(testId, title, key, duration, true);
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

            testDAO.saveQuestionWithAnswers(createdQuestion, testId);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }


    }

    @Override
    public Test receiveTestWithQuestionsAndAnswers(int testId) throws AdminServiceException {
        Test testData;
        try {
            testData = testDAO.getTestInfo(testId);
            Set<Question> questions = testDAO.questionsWithAnswersByTestId(testId);
            testData.setQuestions(questions);
        } catch (DAOException e) {
            throw new AdminServiceException("DB problem", e);
        }

        return testData;
    }

    @Override
    public void changeTestIsEdited(int testId, boolean isEdited) throws AdminServiceException {
        try {
            testDAO.updateTestIsEdited(testId, isEdited);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }


    @Override
    public void updateQuestionWithAnswers(int questionId,
                                          String question,
                                          String deletedAnswers,
                                          Map<Integer, String> answers,
                                          Map<Integer, String> addedAnswers,
                                          List<Integer> rightAnswersId,
                                          List<Integer> rightAddedAnswersId) throws AdminServiceException {

        Question updatedQuestion = new Question(questionId, question);
        Set<Answer> answerToUpdate = new HashSet<>();
        Set<Answer> answerToAdd = new HashSet<>();

        List<Integer> answerToDelete = new ArrayList<>();

        if (!deletedAnswers.equals("")) {
            String[] answersIdToDelete = deletedAnswers.split(",");
            for (String s : answersIdToDelete) {
                answerToDelete.add(Integer.parseInt(s));
            }
        }

        answers.forEach((k, v) -> {
            Answer answer = new Answer(k, v);
            rightAnswersId.forEach(value -> {
                if (value.equals(k)) {
                    answer.setResult(true);
                }
            });
            answerToUpdate.add(answer);
        });

        addedAnswers.forEach((k, v) -> {
            Answer answer = new Answer(v);
            rightAddedAnswersId.forEach(value -> {
                if (value.equals(k)) {
                    answer.setResult(true);
                }
            });
            answerToAdd.add(answer);
        });

        try {
            testDAO.updateQuestionWithAnswersByQuestionId(updatedQuestion, answerToUpdate, answerToAdd, answerToDelete, LocalDate.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }

    @Override
    public void completeTestCreation(int testID) throws AdminServiceException {

        try {
            testDAO.updateTestIsEdited(testID, false);
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }

    @Override
    public void deleteQuestionWithAnswers(int questionId) throws AdminServiceException {

        try {
            testDAO.deleteQuestionWithAnswers(questionId, LocalDateTime.now());
        } catch (DAOSqlException e) {
            throw new AdminServiceException("DB problem", e);
        }
    }

    @Override
    public void addTestType(String testTypeTitle) throws AdminServiceException, ExistsTypeAdminServiceException {
        TestTypeDAO typeDao = daoFactory.getTypeDao();

        try {
            Type typeByTitle = typeDao.getTypeByTitle(testTypeTitle);
            if (typeByTitle.getTitle() != null) {
                throw new ExistsTypeAdminServiceException("Exists test type exception in AdminService addTestType() method");
            }

            typeDao.saveTestType(testTypeTitle);

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
