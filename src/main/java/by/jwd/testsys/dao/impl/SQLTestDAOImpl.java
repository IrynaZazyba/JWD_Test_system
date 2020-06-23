package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestDAO;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class SQLTestDAOImpl implements TestDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();


    private static final String SELECT_TEST_INFO_BY_ID = "SELECT t.title, t.key, t.time, " +
            "type.id as type_id FROM test t inner join type on type.id=t.type_id  WHERE t.id=? AND t.deleted_at is NULL";


    private static final String SELECT_KEY_TO_TEST = "SELECT `key` FROM `test` WHERE id=?";

    private static final String SELECT_TEST_TIME = "SELECT time FROM `test` WHERE id=(" +
            "SELECT test_id FROM assignment WHERE id=?)";

    private static final String GET_ASSIGNMENT_TESTS = "SELECT test.id as t_id, test.title as t_title, test.time," +
            " count(question.id) as count_quest, type.title as type_title FROM test inner join question on question.test_id=test.id " +
            "INNER JOIN assignment on assignment.test_id=test.id inner join type on test.type_id=type.id where assignment.user_id =? " +
            "and completed is false and question.deleted_at is null and assignment.deleted_at is null group BY test.id";

    private static final String SELECT_TESTS_BY_TYPE_ID = "SELECT id, title,`key`, time FROM `test` WHERE type_id=? " +
            "and deleted_at is null AND is_edited=?";

    private static final String UPDATE_TEST_DELETED_AT = "UPDATE `test` SET `deleted_at`=? where id=? and deleted_at is null";

    private static final String UPDATE_TEST_EDITED = "UPDATE `test` SET `is_edited`=? where id=?";

    private static final String GET_COUNT_INCOMPLETE_ASSIGNMENT_BY_TEST_ID = "SELECT count(id) as count_id FROM `assignment` " +
            "WHERE test_id=? and completed=?";

    private static final String DELETE_QUESTIONS_BY_TEST_ID = "UPDATE `question` set deleted_at=? WHERE test_id=?";


    private static final String DELETE_ANSWER_BY_QUESTION_ID = "UPDATE `answer` set deleted_at=? WHERE question_id=?";

    private static final String GET_QUESTIONS_ID_TO_TEST = "SELECT id from question where test_id=?";

    private static final String INSERT_TEST = "INSERT INTO `test` (`title`, `key`, `time`, `type_id`, `is_edited`) " +
            "VALUES (?,?,?,?,?)";


    private static final String UPDATE_TEST = "UPDATE test SET title=?, `key`=?, time=?,type_id=?, " +
            "is_edited=? WHERE id=?";

    private static final String SELECT_ALL_TESTS_BY_TYPE_ID_LIMIT_PAGE = "SELECT id, title,`key`, time FROM `test` " +
            "WHERE type_id=? and deleted_at is null LIMIT ?,?";

    private static final String GET_COUNT_TESTS_BY_TYPE_ID = "SELECT count(id) as countRow FROM `test` " +
            "WHERE type_id=? and deleted_at is null";


    private static final String TEST_ID_COLUMN = "id";
    private static final String TEST_TITLE_COLUMN = "title";
    private static final String TEST_KEY_COLUMN = "key";
    private static final String TEST_TIME_COLUMN = "time";
    private static final String COUNT_TEST_ASSIGNMENT = "count_id";
    private static final String COUNT_TEST_ROW = "countRow";
    private static final String TEST_ID_COLUMN_ALIAS = "t_id";
    private static final String TEST_TITLE_COLUMN_ALIAS = "t_title";
    private static final String TYPE_TITLE_COLUMN_ALIAS = "type_title";
    private static final String TYPE_ID_COLUMN_ALIAS = "type_id";
    private static final String QUESTION_ID_COLUMN = "id";
    private static final String COUNT_QUESTION = "count_quest";


    @Override
    public Set<Test> getAssignedTests(int userId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Test> assignmentTests = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_ASSIGNMENT_TESTS);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Test test = buildTestWithType(resultSet);
                assignmentTests.add(test);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("Exception in SQLTestDAOImpl method getAssignedTests", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getAssignedTests", e);
            throw new DAOSqlException("Exception in SQLTestDAOImpl method getAssignedTests", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return assignmentTests;
    }


    @Override
    public Test getTestInfo(int id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Test test = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_INFO_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (test == null) {
                    test = buildTest(resultSet);

                    int typeId = resultSet.getInt(TYPE_ID_COLUMN_ALIAS);
                    Type type = new Type.Builder().withId(typeId).build();

                    test.setType(type);
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTestInfo()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestInfo()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestInfo()", e);

        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return test;
    }


    @Override
    public String getTestKey(int testId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String key = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_KEY_TO_TEST);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                key = resultSet.getString(TEST_KEY_COLUMN);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTestKey()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestKey()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestKey()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return key;
    }


    @Override
    public LocalTime getTestDuration(int assignmentId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        LocalTime duration = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TEST_TIME);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Time time = resultSet.getTime(TEST_TIME_COLUMN);
                duration = time.toLocalTime();
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestStartDateTime()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return duration;
    }

    @Override
    public Set<Test> getTests(int typeId, boolean isEdited) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Test> tests = new HashSet<>();

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TESTS_BY_TYPE_ID);
            preparedStatement.setInt(1, typeId);
            preparedStatement.setBoolean(2, isEdited);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Test test = buildTest(resultSet);
                int id = resultSet.getInt(TEST_ID_COLUMN);
                test.setEdited(isEdited);
                test.setId(id);
                tests.add(test);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTests()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTests()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTests()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return tests;
    }

    @Override
    public Set<Test> getTestsByLimit(int typeId, int from, int to) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Test> tests = new HashSet<>();

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL_TESTS_BY_TYPE_ID_LIMIT_PAGE);
            preparedStatement.setInt(1, typeId);
            preparedStatement.setInt(2, from);
            preparedStatement.setInt(3, to);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Test test = buildTest(resultSet);
                int id = resultSet.getInt(TEST_ID_COLUMN);
                test.setId(id);
                tests.add(test);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getTestsByLimit()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getTestsByLimit()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getTestsByLimit()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return tests;
    }

    //todo передавать deleted
    @Override
    public int getCountTests(int typeId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int countNotDeletedTests = 0;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(GET_COUNT_TESTS_BY_TYPE_ID);
            preparedStatement.setInt(1, typeId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countNotDeletedTests = resultSet.getInt(COUNT_TEST_ROW);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountTests()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getCountTests()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getCountTests()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return countNotDeletedTests;
    }


    @Override
    public int getCountTestAssignment(int testId, boolean isCompleted) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int countAssignment = 0;
        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(GET_COUNT_INCOMPLETE_ASSIGNMENT_BY_TEST_ID);
            preparedStatement.setInt(1, testId);
            preparedStatement.setBoolean(2,isCompleted);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countAssignment = resultSet.getInt(COUNT_TEST_ASSIGNMENT);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getCountTestAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getCountTestAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getCountTestAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return countAssignment;
    }

    @Override
    public int saveTest(Test test, int typeId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKey = null;
        int createdTestId = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_TEST, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, test.getKey());
            preparedStatement.setTime(3, Time.valueOf(test.getDuration()));
            preparedStatement.setInt(4, typeId);
            preparedStatement.setBoolean(5, test.isEdited());
            preparedStatement.executeUpdate();

            generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                createdTestId = generatedKey.getInt(1);
            }

        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method saveTest()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method saveTest()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method saveTest()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKey);
        }
        return createdTestId;
    }


    @Override
    public void deleteTestById(int testId, LocalDateTime deletedDate) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = connectionPool.takeConnection();
            connection.setAutoCommit(false);

            Set<Integer> questionsId = new HashSet<>();

            preparedStatement = connection.prepareStatement(GET_QUESTIONS_ID_TO_TEST);
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                questionsId.add(resultSet.getInt(QUESTION_ID_COLUMN));
            }

            for (Integer questId : questionsId) {
                preparedStatement = connection.prepareStatement(DELETE_ANSWER_BY_QUESTION_ID);
                preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
                preparedStatement.setInt(2, questId);
                preparedStatement.executeUpdate();
            }

            preparedStatement = connection.prepareStatement(DELETE_QUESTIONS_BY_TEST_ID);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement(UPDATE_TEST_DELETED_AT);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(deletedDate));
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLTestDAOImpl method deleteTestById()", e);
                throw new DAOSqlException("Impossible to rollback SQLTestDAOImpl method deleteTestById()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method deleteTestById()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method deleteTestById()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method deleteTestById()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
    }


    @Override
    public void updateTest(Test test, int typeId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_TEST);
            preparedStatement.setString(1, test.getTitle());
            preparedStatement.setString(2, test.getKey());
            preparedStatement.setTime(3, Time.valueOf(test.getDuration()));
            preparedStatement.setInt(4, typeId);
            preparedStatement.setBoolean(5, test.isEdited());
            preparedStatement.setInt(6, test.getId());
            preparedStatement.executeUpdate();


        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method updateTest()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateTest()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateTest()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public void updateTestIsEdited(int testId, boolean isEdited) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_TEST_EDITED);
            preparedStatement.setBoolean(1, isEdited);
            preparedStatement.setInt(2, testId);
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method updateTestIsEdited()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateTestIsEdited()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateTestIsEdited()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }


    private Test buildTest(ResultSet resultSet) throws SQLException {
        String title = resultSet.getString(TEST_TITLE_COLUMN);
        String key = resultSet.getString(TEST_KEY_COLUMN);
        Time time = resultSet.getTime(TEST_TIME_COLUMN);
        LocalTime testDuration = null;
        if (time != null) {
            testDuration = time.toLocalTime();
        }
        return new Test.Builder().withTitle(title).withKey(key).withDuration(testDuration).build();

    }

    private Test buildTestWithType(ResultSet resultSet) throws SQLException {
        int testId = resultSet.getInt(TEST_ID_COLUMN_ALIAS);
        String testTitle = resultSet.getString(TEST_TITLE_COLUMN_ALIAS);
        Time time = resultSet.getTime(TEST_TIME_COLUMN);
        LocalTime duration = null;
        if (time != null) {
            duration = time.toLocalTime();
        }
        String typeTitle = resultSet.getString(TYPE_TITLE_COLUMN_ALIAS);
        int countQuestion = resultSet.getInt(COUNT_QUESTION);
        Type testType = new Type();
        testType.setTitle(typeTitle);
        return new Test.Builder()
                .withId(testId)
                .withTitle(testTitle)
                .withType(testType)
                .withDuration(duration)
                .withCountQuestion(countQuestion).build();
    }
}
