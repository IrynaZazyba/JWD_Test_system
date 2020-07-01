package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Answer;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.dao.QuestionAnswerDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SQLQuestionAnswerDAOImpl implements QuestionAnswerDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private static final String SELECT_QUESTION = "SELECT id, question FROM question q WHERE test_id=? " +
            "and not EXISTS (select * from `question-log` where q.id=question_id  and assignment_id=?) Limit 1;";

    private static final String SELECT_ANSWER_BY_QUESTION_ID = "SELECT a.id, a.answer FROM answer a WHERE question_id=?";

    private static final String GET_COUNT_QUESTION_BY_TEST_ID = "SELECT count(id) as count_question FROM `question` " +
            "WHERE test_id=?";

    private static final String SELECT_RIGHT_ANSWERS_TO_QUESTION = "SELECT q.id q_id, a.id  a_id FROM question q " +
            "INNER JOIN answer a ON q.id=a.question_id WHERE test_id=? AND result=1";

    private static final String INSERT_QUESTION = "INSERT INTO `question` (`question`, `test_id`) " +
            "VALUES (?,?)";

    private static final String INSERT_ANSWER = "INSERT INTO `answer` (`answer`, `result`, `question_id`) " +
            "VALUES (?,?,?)";

    private static final String UPDATE_QUESTION_TITLE = "UPDATE `question` SET `question`=? WHERE id=?";

    private static final String UPDATE_ANSWER_DELETED_AT_BY_ANSWER_ID = "UPDATE `answer` SET `deleted_at`=? WHERE id=?";

    private static final String UPDATE_ANSWER_TITLE_RESULT_BY_ANSWER_ID = "UPDATE `answer` SET `answer`=?,`result`=? WHERE id=?;";

    private static final String SELECT_QUESTION_WITH_ANSWER_BY_TEST_ID = "SELECT question, question.id as q_id, " +
            "answer.id as a_id, answer, result FROM `question` inner join answer on answer.question_id=question.id " +
            "WHERE question.deleted_at is NULL and answer.deleted_at is NULL and test_id=?";

    private static final String DELETE_QUESTION_BY_QUESTION_ID = "UPDATE `question` set deleted_at=? WHERE id=?";

    private static final String DELETE_ANSWER_BY_QUESTION_ID = "UPDATE `answer` set deleted_at=? WHERE question_id=?";


    private static final String QUESTION_ID_COLUMN = "id";
    private static final String QUESTION_QUESTION_COLUMN = "question";
    private static final String ANSWER_ID_COLUMN = "id";
    private static final String ANSWER_ANSWER_COLUMN = "answer";
    private static final String QUESTION_ID_COLUMN_ALIAS = "q_id";
    private static final String ANSWER_ID_COLUMN_ALIAS = "a_id";
    private static final String ANSWER_RESULT_COLUMN = "result";


    private static final String COUNT_QUESTION = "count_question";


    @Override
    public Question getQuestionByTestId(int id, int assigment_id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Question question = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_QUESTION);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, assigment_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                question = new Question();
                question.setId(resultSet.getInt(QUESTION_ID_COLUMN));
                question.setQuestion(resultSet.getString(QUESTION_QUESTION_COLUMN));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPool exception in getQuestionByTestId() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in getQuestionByTestId() method", e);
            throw new DAOSqlException("SQLException in getQuestionByTestId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return question;
    }


    @Override
    public Set<Answer> getAnswersByQuestionId(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Answer> answers = new HashSet<>();
        Answer answer;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_ANSWER_BY_QUESTION_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                answer = new Answer();
                answer.setId(resultSet.getInt(ANSWER_ID_COLUMN));
                answer.setAnswer(resultSet.getString(ANSWER_ANSWER_COLUMN));
                answers.add(answer);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("Exception in SQLTestDAOImpl method getAnswersByQuestionId()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getAnswersByQuestionId()", e);
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAnswersByQuestionId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return answers;
    }


    @Override
    public int getCountQuestion(int testId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int countQuestion = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_COUNT_QUESTION_BY_TEST_ID);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countQuestion = resultSet.getInt(COUNT_QUESTION);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("Exception in SQLTestDAOImpl method getCountQuestion()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountQuestion()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getCountQuestion()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return countQuestion;
    }

    @Override
    public Map<Integer, List<Integer>> getRightAnswersToQuestionByTestId(int testId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<Integer, List<Integer>> questionAnswers = new HashMap<>();
        List<Integer> listAnswers;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_RIGHT_ANSWERS_TO_QUESTION);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int q_id = resultSet.getInt(QUESTION_ID_COLUMN_ALIAS);
                if (questionAnswers.containsKey(q_id)) {
                    List<Integer> answers = questionAnswers.get(q_id);
                    answers.add(resultSet.getInt(ANSWER_ID_COLUMN_ALIAS));
                } else {
                    int a_id = resultSet.getInt(ANSWER_ID_COLUMN_ALIAS);
                    listAnswers = new ArrayList<>();
                    listAnswers.add(a_id);
                    questionAnswers.put(q_id, listAnswers);
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getRightAnswersToQuestionByTestId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return questionAnswers;
    }

    @Override
    public int saveQuestionWithAnswers(Question question, int testId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKey = null;
        int createdQuestionId = 0;

        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();

            generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                createdQuestionId = generatedKey.getInt(1);
            }

            for (Answer answer : question.getAnswers()) {
                preparedStatement = connection.prepareStatement(INSERT_ANSWER);
                preparedStatement.setString(1, answer.getAnswer());
                preparedStatement.setBoolean(2, answer.isResult());
                preparedStatement.setInt(3, createdQuestionId);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method deleteTestById()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method deleteTestById()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method saveQuestionWithAnswers()", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method saveQuestionWithAnswers() in" +
                            "attempt to setAutoCommit(true)", e);
                }
            }
            connectionPool.closeConnection(connection, preparedStatement, generatedKey);
        }
        return createdQuestionId;
    }


    @Override
    public void updateQuestionWithAnswersByQuestionId(Question updatedQuestion,
                                                      Set<Answer> answerToUpdate,
                                                      Set<Answer> answerToAdd,
                                                      List<Integer> answerToDelete,
                                                      LocalDate deletedDate) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(UPDATE_QUESTION_TITLE);
            preparedStatement.setString(1, updatedQuestion.getQuestion());
            preparedStatement.setInt(2, updatedQuestion.getId());
            preparedStatement.executeUpdate();

            if (answerToDelete.size() != 0) {
                for (Integer id : answerToDelete) {
                    preparedStatement = connection.prepareStatement(UPDATE_ANSWER_DELETED_AT_BY_ANSWER_ID);
                    preparedStatement.setDate(1, Date.valueOf(deletedDate));
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();
                }
            }

            for (Answer answer : answerToAdd) {
                preparedStatement = connection.prepareStatement(INSERT_ANSWER);
                preparedStatement.setString(1, answer.getAnswer());
                preparedStatement.setBoolean(2, answer.isResult());
                preparedStatement.setInt(3, updatedQuestion.getId());
                preparedStatement.executeUpdate();
            }

            for (Answer answer : answerToUpdate) {
                preparedStatement = connection.prepareStatement(UPDATE_ANSWER_TITLE_RESULT_BY_ANSWER_ID);
                preparedStatement.setString(1, answer.getAnswer());
                preparedStatement.setBoolean(2, answer.isResult());
                preparedStatement.setInt(3, answer.getId());
                preparedStatement.executeUpdate();
            }

            connection.commit();

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId()", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId()", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateQuestionWithAnswersByQuestionId() in" +
                            "attempt to setAutoCommit(true)", e);
                }
            }
            connectionPool.closeConnection(connection, preparedStatement);
        }

    }

    @Override
    public Set<Question> getQuestionsWithAnswersByTestId(int testId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Question question = null;
        Set<Question> questionsWithAnswers = new HashSet<>();
        Set<Answer> answersToQuestion = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_QUESTION_WITH_ANSWER_BY_TEST_ID);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int q_id = resultSet.getInt(QUESTION_ID_COLUMN_ALIAS);
                if (question != null && q_id == question.getId()) {
                    Answer answer = buildAnswer(resultSet);
                    answersToQuestion.add(answer);
                } else {
                    String dbQuestion = resultSet.getString(QUESTION_QUESTION_COLUMN);
                    question = new Question.Builder().withId(q_id).withQuestion(dbQuestion).build();
                    questionsWithAnswers.add(question);
                    answersToQuestion = new HashSet<>();
                    Answer answer = buildAnswer(resultSet);
                    answersToQuestion.add(answer);
                    question.setAnswers(answersToQuestion);
                }
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getQuestionsWithAnswersByTestId()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getQuestionsWithAnswersByTestId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getQuestionsWithAnswersByTestId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return questionsWithAnswers;
    }


    private Answer buildAnswer(ResultSet resultSet) throws SQLException {
        int a_id = resultSet.getInt(ANSWER_ID_COLUMN_ALIAS);
        String dbAnswer = resultSet.getString(ANSWER_ANSWER_COLUMN);
        boolean result = resultSet.getBoolean(ANSWER_RESULT_COLUMN);
        return new Answer.Builder().withId(a_id).withAnswer(dbAnswer).withResult(result).build();
    }

    @Override
    public void deleteQuestionWithAnswers(int questionId, LocalDateTime deletedDate) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(DELETE_QUESTION_BY_QUESTION_ID);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, questionId);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(DELETE_ANSWER_BY_QUESTION_ID);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, questionId);
            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method deleteQuestionWithAnswers()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method deleteQuestionWithAnswers()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method deleteQuestionWithAnswers()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method deleteQuestionWithAnswers()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method deleteQuestionWithAnswers()", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method deleteQuestionWithAnswers() in" +
                            "attempt to setAutoCommit(true)", e);
                }
            }
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }


}
