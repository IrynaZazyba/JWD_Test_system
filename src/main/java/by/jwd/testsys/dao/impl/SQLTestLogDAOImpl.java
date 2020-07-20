package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class SQLTestLogDAOImpl implements TestLogDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getSqlConnectionPoolDAO();

    private static final String INSERT_ANSWER_LOG = "INSERT INTO `answer-log` (`answer_id`, `question_log_id`) " +
            "VALUES (?,?);";

    private static final String INSERT_QUESTION_LOG = "INSERT INTO `question-log`(`question_id`,`assignment_id`)" +
            " VALUES (?,?)";

    private static final String SELECT_TEST_LOG_BY_ASSIGNMENT_ID = "SELECT q.question_id q_id,a.answer_id a_id FROM " +
            "`question-log` as q INNER JOIN `answer-log` as a on q.id=a.question_log_id WHERE assignment_id=?";


    private static final String QUESTION_LOG_ID_COLUMN_ALIAS="q_id";
    private static final String ANSWER_LOG_ID_COLUMN_ALIAS="a_id";


    @Override
    public void writeAnswerLog(int questionLogId, Set<Integer> answers) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(INSERT_ANSWER_LOG);
            for (int answerId : answers) {
                preparedStatement.setInt(1, answerId);
                preparedStatement.setInt(2, questionLogId);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback answersLog SQLTestLogDAOImpl method writeAnswerLog()", e);
                throw new DAOSqlException("Impossible to rollback answersLog SQLTestLogDAOImpl method writeAnswerLog()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeAnswerLog()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method writeAnswerLog()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestLogDAOImpl method writeAnswerLog()", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeAnswerLog() in" +
                            "attempt to setAutoCommit(true)", e);
                }
            }
            connectionPool.closeConnection(connection, preparedStatement);
        }

    }


    @Override
    public Integer writeQuestionLog(int idQuestion, int idAssignment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Integer questionLogId = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_QUESTION_LOG, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, idQuestion);
            preparedStatement.setInt(2, idAssignment);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                questionLogId = generatedKeys.getInt(1);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return questionLogId;
    }

    @Override
    public TestLog getTestLog(int assignmentId) throws DAOException {
        TestLog testLog = new TestLog();
        List<Integer> answers;
        Map<Integer, List<Integer>> questionAnswerMap = new HashMap<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_LOG_BY_ASSIGNMENT_ID);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int question_id = resultSet.getInt(QUESTION_LOG_ID_COLUMN_ALIAS);

                if (questionAnswerMap.containsKey(question_id)) {
                    int answer_id = resultSet.getInt(ANSWER_LOG_ID_COLUMN_ALIAS);
                    List<Integer> answersByKey = questionAnswerMap.get(question_id);
                    answersByKey.add(answer_id);
                } else {
                    int answer_id = resultSet.getInt(ANSWER_LOG_ID_COLUMN_ALIAS);
                    answers = new ArrayList<>();
                    answers.add(answer_id);
                    questionAnswerMap.put(question_id, answers);
                }

            }
            testLog.setAssignment_id(assignmentId);
            testLog.setQuestionAnswerMap(questionAnswerMap);

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeAnswerLog()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return testLog;
    }


}
