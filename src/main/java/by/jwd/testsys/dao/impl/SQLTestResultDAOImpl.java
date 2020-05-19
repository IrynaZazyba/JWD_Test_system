package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Statistic;
import by.jwd.testsys.dao.TestResultDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class SQLTestResultDAOImpl implements TestResultDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    //todo не надоли здесь незаконченные тесты?
    private final static String SELECT_TEST_RESULT = "SELECT id, date_start,date_end,right_count_quest FROM `result`" +
            " where assignment_id=?";

    private static final String INSERT_RESULT = "INSERT INTO `result`(`date_start`,`right_count_quest`," +
            "`all_count_question`, `assignment_id`) VALUES (?,?,?,?)";

    private static final String UPDATE_RESULT_TABLE = "UPDATE result SET date_end=?,right_count_quest=? WHERE id=?";

    private static final String SELECT_COMPLETED_TEST_RESULT = "SELECT title, time, result.date_start as d_start, " +
            "result.date_end as d_end, result.right_count_quest as right_count ,result.all_count_question as all_count " +
            "from result inner join(SELECT assignment.id as asgmt_id, title, time FROM assignment " +
            "inner join test on assignment.test_id=test.id where user_id=? and completed='1') as temp " +
            "on result.assignment_id=temp.asgmt_id";

    @Override
    public Result getTestResult(Assignment assignment) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Result result = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_RESULT);
            preparedStatement.setInt(1, assignment.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                Timestamp date_start = resultSet.getTimestamp("date_start");
                LocalDateTime dateStart = date_start.toLocalDateTime();
                Timestamp date_end = resultSet.getTimestamp("date_end");
                LocalDateTime dateEnd = null;
                if (date_end != null) {
                    dateEnd = date_end.toLocalDateTime();
                }
                int rightCountQuestions = resultSet.getInt("right_count_quest");

                result = new Result(id, dateStart, dateEnd, rightCountQuestions, assignment);

            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestResultDAOImpl method getTestResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method getTestResult()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method getTestResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return result;

    }


    @Override
    public void insertResult(Result result) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_RESULT);

            Timestamp timestamp = Timestamp.valueOf(result.getDateStart());
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, result.getRightCountQuestion());
            preparedStatement.setInt(3, result.getCountTestQuestion());
            preparedStatement.setInt(4, result.getAssignment().getId());
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestResultDAOImpl method insertResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method insertResult()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method insertResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }

    }


    @Override
    public void updateResult(Result result) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_RESULT_TABLE);
            Timestamp timestamp = Timestamp.valueOf(result.getDateEnd());
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, result.getRightCountQuestion());
            preparedStatement.setInt(3, result.getId());
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestLogDAOImpl method updateResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method updateResult()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method updateResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Statistic> userTestStatistic = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_COMPLETED_TEST_RESULT);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                LocalTime time = resultSet.getTime("time").toLocalTime();

                Timestamp d_start = resultSet.getTimestamp("d_start");
                LocalDateTime testStartDate = d_start.toLocalDateTime();

                Timestamp d_end = resultSet.getTimestamp("d_end");
                LocalDateTime testEndDate = d_end.toLocalDateTime();

                int rightCountQuestion = resultSet.getInt("right_count");
                int allCountQuestion = resultSet.getInt("all_count");
                Statistic userStatistic = new Statistic(title, time, testStartDate, testEndDate, rightCountQuestion, allCountQuestion);
                userTestStatistic.add(userStatistic);

            }

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);

        }
        return userTestStatistic;

    }


}
