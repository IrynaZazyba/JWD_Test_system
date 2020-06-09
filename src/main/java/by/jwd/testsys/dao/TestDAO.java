package by.jwd.testsys.dao;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TestDAO {

    Set<Test> getAssignmentTest(int userId) throws DAOSqlException;

    Question getQuestionByTestId(int id, int assignment_id) throws DAOException;

    Set<Answer> getAnswersByQuestionId(int id) throws DAOSqlException;

    Test getTestInfo(int id) throws DAOException;

    int getCountQuestion(int testId) throws DAOSqlException;

    void updateAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException;

    Integer writeAssignment(Assignment assignment) throws DAOSqlException;

    Map<Integer, List<Integer>> getRightAnswersToQuestionByTestId(int testId) throws DAOSqlException;

    String getTestKey(int testId) throws DAOSqlException;

    Timestamp getTestStartDateTime(int assignmentId) throws DAOSqlException;

    LocalTime getTestDuration(int assignmentId) throws DAOSqlException;

    Set<Test> getTests(int typeId, boolean isEdited) throws DAOSqlException;

    void makeAssignmentDeleted(int assignmentId, LocalDate deletedAtDate) throws DAOSqlException;

    void deleteTestById(int testId, LocalDateTime deletedDate) throws DAOSqlException;

    int getCountIncompleteTestAssignment(int testId) throws DAOSqlException;

    int saveTest(Test test, int testId) throws DAOSqlException;

    int saveQuestionWithAnswers(Question question, int testId) throws DAOSqlException;

    void updateTest(Test test, int typeId) throws DAOSqlException;

    void updateTestIsEdited(int testId, boolean isEdited) throws DAOSqlException;

    Set<Question> questionsWithAnswersByTestId(int testId) throws DAOSqlException;


    void updateQuestionWithAnswersByQuestionId(Question updatedQuestion,
                                               Set<Answer> answerToUpdate,
                                               Set<Answer> answerToAdd,
                                               List<Integer> answerToDelete,
                                               LocalDate deletedDate) throws DAOSqlException;

    void deleteQuestionWithAnswers(int questionId, LocalDateTime deletedDate) throws DAOSqlException;
}
