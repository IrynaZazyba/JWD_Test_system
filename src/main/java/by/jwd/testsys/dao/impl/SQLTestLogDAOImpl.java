package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SQLTestLogDAOImpl implements TestLogDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private static final String INSERT_ANSWER_LOG = "INSERT INTO `answer-log` (`answer_id`, `question_log_id`) " +
            "VALUES (?,?);";

    private static final String INSERT_QUESTION_LOG = "INSERT INTO `question-log`(`question_id`,`assignment_id`)" +
            " VALUES (?,?)";

    private static final String SELECT_TEST_LOG_BY_ASSIGNMENT_ID = "SELECT q.question_id q_id,a.answer_id a_id FROM " +
            "`question-log` as q INNER JOIN `answer-log` as a on q.id=a.question_log_id WHERE assignment_id=?";

    private static final String SELECT_RESULTS = "SELECT date_end, right_count_quest, all_count_question, assignment_id, " +
            "test.title as t_tt, type.title as tp_tt, first_name,last_name FROM `result` " +
            "inner join `assignment` on result.assignment_id=assignment.id " +
            "inner join test on assignment.test_id=test.id " +
            "inner join type on type.id=test.type_id " +
            "INNER join users on assignment.user_id=users.id " +
            "WHERE   assignment.deleted_at is null and test.deleted_at is null and assignment.completed=1";

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
            connection.setAutoCommit(true);
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
            throw new DAOSqlException("ConnectionPoolException in SQLTestLogDAOImpl method writeAnswerLog()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }

    }


    @Override
    public Integer writeQuestionLog(int idQuestion, int idAssignment) throws DAOSqlException {
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
            throw new DAOSqlException("ConnectionPoolException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return questionLogId;
    }

    @Override
    public TestLog getTestLog(int assignmentId) throws DAOSqlException {
        TestLog testLog = new TestLog();
        List<Integer> answers = null;
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
                int question_id = resultSet.getInt("q_id");

                if (questionAnswerMap.containsKey(question_id)) {
                    int answer_id = resultSet.getInt("a_id");
                    List<Integer> answersByKey = questionAnswerMap.get(question_id);
                    answersByKey.add(answer_id);
                } else {
                    int answer_id = resultSet.getInt("a_id");
                    answers = new ArrayList<>();
                    answers.add(answer_id);
                    questionAnswerMap.put(question_id, answers);
                }

            }
            testLog.setAssignment_id(assignmentId);
            testLog.setQuestionAnswerMap(questionAnswerMap);

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method writeAnswerLog()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method writeQuestionLog()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return testLog;
    }

    @Override
    public Set<Result> getTestResult(int typeId, int testId, int userId, LocalDate date) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Result> results = new HashSet<>();

        String query=SELECT_RESULTS;

        try {
            connection = connectionPool.takeConnection();
            int count=1;

            if (testId != 0) {
                query += " and test_id=?";
            }

            if (userId != 0) {
                query += " and user_id=?";
            }

            if (typeId != 0) {
                query += " and type.id=?";
            }

            if (date != null) {
                query += " and date=?";
            }


            preparedStatement = connection.prepareStatement(query);

            if (testId != 0) {
                preparedStatement.setInt(count, testId);
                count++;
            }

            if (userId != 0) {
                preparedStatement.setInt(count, userId);
                count++;
            }

            if (typeId != 0) {
                preparedStatement.setInt(count, typeId);
                count++;
            }

            if (date != null) {
                preparedStatement.setDate(count, Date.valueOf(date));
            }


            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                String testTitle = resultSet.getString("t_tt");
                String typeTitle = resultSet.getString("tp_tt");
                Type type = new Type();
                type.setTitle(typeTitle);
                Test test = new Test();
                test.setTitle(testTitle);
                test.setType(type);
                LocalDateTime testDate = resultSet.getTimestamp("date_end").toLocalDateTime();
                int rightCountQuestion = resultSet.getInt("right_count_quest");
                int allCountQuestion = resultSet.getInt("all_count_question");
                int assignmentId = resultSet.getInt("assignment_id");
                Assignment assignment = new Assignment();
                assignment.setId(assignmentId);
                Result result = new Result(testDate, rightCountQuestion, allCountQuestion, test, user, assignment);
                results.add(result);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestResult()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method getTestResult()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestLogDAOImpl method getTestResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return results;
    }


}
