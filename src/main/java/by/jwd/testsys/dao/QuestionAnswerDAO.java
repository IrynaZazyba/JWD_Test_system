package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Answer;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QuestionAnswerDAO {


    Question getQuestionByTestId(int id, int assigment_id) throws DAOException;

    Set<Answer> getAnswersByQuestionId(int id) throws DAOException;

    int getCountQuestion(int testId) throws DAOException;

    Map<Integer, List<Integer>> getRightAnswersToQuestionByTestId(int testId) throws DAOException;

    int saveQuestionWithAnswers(Question question, int testId) throws DAOException;

    void updateQuestionWithAnswersByQuestionId(Question updatedQuestion,
                                               Set<Answer> answerToUpdate,
                                               Set<Answer> answerToAdd,
                                               List<Integer> answerToDelete,
                                               LocalDate deletedDate) throws DAOException;

    Set<Question> getQuestionsWithAnswersByTestId(int testId) throws DAOException;

    void deleteQuestionWithAnswers(int questionId, LocalDateTime deletedDate) throws DAOException;
}
