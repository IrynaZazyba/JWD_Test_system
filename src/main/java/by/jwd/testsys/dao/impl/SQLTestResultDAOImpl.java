package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.*;
import by.jwd.testsys.dao.TestResultDAO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class SQLTestResultDAOImpl implements TestResultDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private final static String SELECT_TEST_RESULT = "SELECT id, date_start,date_end,right_count_quest FROM `result`" +
            " where assignment_id=?";

    private static final String INSERT_RESULT = "INSERT INTO `result`(`date_start`,`right_count_quest`," +
            "`all_count_question`, `assignment_id`) VALUES (?,?,?,?)";

    private static final String UPDATE_RESULT_TABLE = "UPDATE result SET date_end=?,right_count_quest=? WHERE id=?";

    private static final String SELECT_COMPLETED_TEST_RESULT = "SELECT title, time, date_start,date_end, " +
            "right_count_quest,all_count_question from result " +
            "inner join(SELECT assignment.id as asgmt_id, title, time FROM assignment " +
            "inner join test on assignment.test_id=test.id where user_id=? and completed='1') as temp " +
            "on result.assignment_id=temp.asgmt_id";

    private static final String SELECT_RESULTS = "SELECT date_end, right_count_quest, all_count_question, assignment_id, " +
            "test.title as test_tt, type.title as type_tt, first_name,last_name FROM `result` " +
            "inner join `assignment` on result.assignment_id=assignment.id " +
            "inner join test on assignment.test_id=test.id " +
            "inner join type on type.id=test.type_id " +
            "INNER join users on assignment.user_id=users.id " +
            "WHERE assignment.deleted_at is null and assignment.completed=1";

    private static final String SELECT_START_TEST_DATE_TIME = "SELECT date_start FROM `result` WHERE assignment_id=?";

    private final static String SQL_CONDITION_TEST_ID = " and test_id=?";
    private final static String SQL_CONDITION_USER_ID = " and user_id=?";
    private final static String SQL_CONDITION_TYPE_TABLE_ID = " and type.id=?";
    private final static String SQL_CONDITION_DATE = " and date=?";

    private static final String RESULT_ID_COLUMN = "id";
    private static final String RESULT_DATE_START_COLUMN = "date_start";
    private static final String RESULT_DATE_END_COLUMN = "date_end";
    private static final String RESULT_RIGHT_COUNT_QUEST_COLUMN = "right_count_quest";
    private static final String RESULT_ALL_COUNT_QUEST_COLUMN = "all_count_question";
    private static final String RESULT_ASSIGNMENT_ID_COLUMN = "assignment_id";

    private static final String TEST_TIME_COLUMN = "time";
    private static final String TEST_TITLE_COLUMN = "title";
    private static final String USER_FIRST_NAME_COLUMN = "first_name";
    private static final String USER_LAST_NAME_COLUMN = "last_name";

    private static final String TEST_TITLE_COLUMN_ALIAS = "test_tt";
    private static final String TYPE_TITLE_COLUMN_ALIAS = "type_tt";


    @Override
    public Result getTestResult(Assignment assignment) throws DAOException {
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
                int id = resultSet.getInt(RESULT_ID_COLUMN);
                Timestamp date_start = resultSet.getTimestamp(RESULT_DATE_START_COLUMN);
                LocalDateTime dateStart = date_start.toLocalDateTime();
                Timestamp date_end = resultSet.getTimestamp(RESULT_DATE_END_COLUMN);
                LocalDateTime dateEnd = null;
                if (date_end != null) {
                    dateEnd = date_end.toLocalDateTime();
                }
                int rightCountQuestions = resultSet.getInt(RESULT_RIGHT_COUNT_QUEST_COLUMN);

                result = new Result.Builder()
                        .withId(id)
                        .withDateStart(dateStart)
                        .withDateEnd(dateEnd)
                        .withRightCountQuestion(rightCountQuestions)
                        .withAssignment(assignment)
                        .build();
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestResultDAOImpl method getTestResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method getTestResult()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method getTestResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return result;

    }

    @Override
    public Timestamp getTestStartDateTime(int assignmentId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Timestamp dateStart = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_START_TEST_DATE_TIME);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                dateStart = resultSet.getTimestamp(RESULT_DATE_START_COLUMN);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return dateStart;
    }

    @Override
    public void insertResult(Result result) throws DAOException {
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestResultDAOImpl method insertResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method insertResult()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method insertResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }

    }


    @Override
    public void updateResult(Result result) throws DAOException {
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestLogDAOImpl method updateResult()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method updateResult()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method updateResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public Set<Statistic> getUserTestStatistic(int userId) throws DAOException {
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
                String title = resultSet.getString(TEST_TITLE_COLUMN);
                Time dbTime = resultSet.getTime(TEST_TIME_COLUMN);
                LocalTime timeOnTest = null;
                if (dbTime != null) {
                    timeOnTest = dbTime.toLocalTime();
                }

                Timestamp d_start = resultSet.getTimestamp(RESULT_DATE_START_COLUMN);
                LocalDateTime testStartDate = buildDateTime(d_start);

                Timestamp d_end = resultSet.getTimestamp(RESULT_DATE_END_COLUMN);
                LocalDateTime testEndDate = buildDateTime(d_end);

                int rightCountQuestion = resultSet.getInt(RESULT_RIGHT_COUNT_QUEST_COLUMN);
                int allCountQuestion = resultSet.getInt(RESULT_ALL_COUNT_QUEST_COLUMN);

                Statistic userStatistic = new Statistic.Builder().withTestTitle(title)
                        .withTimeOnTest(timeOnTest)
                        .withTestStart(testStartDate)
                        .withTestEnd(testEndDate)
                        .withRightCountQuestion(rightCountQuestion)
                        .withAllCountQuestion(allCountQuestion).build();
                userTestStatistic.add(userStatistic);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
            throw new DAOSqlException("SQLException in SQLTestResultDAOImpl method getUserTestStatistic()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return userTestStatistic;

    }


    @Override
    public Set<Result> getTestResult(int typeId, int testId, int userId, LocalDate date) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Result> results = new HashSet<>();

        String query = SELECT_RESULTS;

        try {
            connection = connectionPool.takeConnection();
            int count = 1;

            if (testId != 0) {
                query += SQL_CONDITION_TEST_ID;
            }

            if (userId != 0) {
                query += SQL_CONDITION_USER_ID;
            }

            if (typeId != 0) {
                query += SQL_CONDITION_TYPE_TABLE_ID;
            }

            if (date != null) {
                query += SQL_CONDITION_DATE;
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
                String firstName = resultSet.getString(USER_FIRST_NAME_COLUMN);
                String lastName = resultSet.getString(USER_LAST_NAME_COLUMN);
                User user = new User.Builder().withFirstName(firstName).withLastName(lastName).build();

                String testTitle = resultSet.getString(TEST_TITLE_COLUMN_ALIAS);
                String typeTitle = resultSet.getString(TYPE_TITLE_COLUMN_ALIAS);
                Type type = new Type();
                type.setTitle(typeTitle);
                Test test = new Test();
                test.setTitle(testTitle);
                test.setType(type);

                Timestamp date_end = resultSet.getTimestamp(RESULT_DATE_END_COLUMN);
                LocalDateTime dateEnd = buildDateTime(date_end);
                int rightCountQuestion = resultSet.getInt(RESULT_RIGHT_COUNT_QUEST_COLUMN);
                int allCountQuestion = resultSet.getInt(RESULT_ALL_COUNT_QUEST_COLUMN);
                int assignmentId = resultSet.getInt(RESULT_ASSIGNMENT_ID_COLUMN);
                Assignment assignment = new Assignment.Builder().withId(assignmentId).build();

                Result result = new Result.Builder().withDateEnd(dateEnd)
                        .withRightCountQuestion(rightCountQuestion)
                        .withCountTestQuestion(allCountQuestion)
                        .withTest(test)
                        .withUser(user)
                        .withAssignment(assignment).build();
                results.add(result);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestLogDAOImpl method getTestResult()", e);
            throw new DAOSqlException("SQLException in SQLTestLogDAOImpl method getTestResult()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestLogDAOImpl method getTestResult()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);

        }
        return results;
    }


    private LocalDateTime buildDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }


}
