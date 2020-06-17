package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLTestTypeDAOImpl implements TestTypeDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private static final String SELECT_ALL_TYPES = "SELECT id, title from type WHERE deleted_at IS null";
    private static final String SELECT_TYPE_BY_TITLE = "SELECT id, title from type WHERE deleted_at IS null AND title=?";


    private static final String SELECT_TYPE_WITH_TESTS = "SELECT type.id as tp_id, type.title tp_title, " +
            "test.time as tt_time,test.id tt_id, test.title tt_title, count(question.id) as count_quest " +
            "FROM type INNER JOIN test ON test.type_id=type.id inner join question on question.test_id=test.id " +
            "WHERE type.deleted_at IS null AND test.deleted_at IS null AND question.deleted_at is null " +
            "and test.key is null group by test.id";

    private static final String INSERT_TYPE = "INSERT INTO type (title) VALUES (?)";

    @Override
    public List<Type> getAll() throws DAOException {

        Connection connection = null;
        List<Type> typesFromDB;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_TYPES);
            typesFromDB = new ArrayList<>();
            while (resultSet.next()) {
                int typeId = resultSet.getInt("id");
                String typeTitle = resultSet.getString("title");
                typesFromDB.add(new Type(typeId, typeTitle));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestTypeDAOImpl getAll() method.", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl getAll() method", e);
            throw new DAOSqlException("SQLException in SQLTestTypeDAOImpl getAll() method.", e);
        } finally {
            connectionPool.closeConnection(connection, statement, resultSet);
        }

        return typesFromDB;
    }

    @Override
    public Set<Type> getTypeWithTests() throws DAOException {
        Connection connection = null;
        Set<Type> typesFromDB = new HashSet<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_TYPE_WITH_TESTS);
            while (resultSet.next()) {
                Type type = buildType(resultSet);
                typesFromDB.add(type);

                int type_id = resultSet.getInt("tp_id");
                Test test = buildTest(resultSet);
                for (Type type1 : typesFromDB) {
                    if (type1.getId() == type_id) {
                        type1.setTests(test);
                    }
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestTypeDAOImpl getTypeWithTests() method.", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl getTypeWithTests() method", e);
            throw new DAOSqlException("SQLException in SQLTestTypeDAOImpl getTypeWithTests() method.", e);

        } finally {
            connectionPool.closeConnection(connection, statement, resultSet);
        }

        return typesFromDB;
    }

    @Override
    public void saveTestType(String testTypeTitle) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_TYPE);
            preparedStatement.setString(1, testTypeTitle);
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestTypeDAOImpl saveTestType() method.", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl saveTestType() method", e);
            throw new DAOSqlException("SQLException in SQLTestTypeDAOImpl saveTestType() method.", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }


    @Override
    public Type getTypeByTitle(String title) throws DAOSqlException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Type type = new Type();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_TYPE_BY_TITLE);
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String typeTitle = resultSet.getString("title");
                type.setId(id);
                type.setTitle(typeTitle);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestTypeDAOImpl getTypeByTitle() method.", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl getTypeByTitle() method", e);
            throw new DAOSqlException("SQLException in SQLTestTypeDAOImpl getTypeByTitle() method.", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return type;
    }

    private Type buildType(ResultSet resultSet) throws SQLException {
        int idType = resultSet.getInt("tp_id");
        String titleType = resultSet.getString("tp_title");
        return new Type(idType, titleType);
    }

    private Test buildTest(ResultSet resultSet) throws SQLException {
        int idTest = resultSet.getInt("tt_id");
        String titleTest = resultSet.getString("tt_title");
        LocalTime testDuration = resultSet.getTime("tt_time").toLocalTime();
        int countQuestion = resultSet.getInt("count_quest");
        return new Test(idTest, titleTest, testDuration, countQuestion);
    }
}
