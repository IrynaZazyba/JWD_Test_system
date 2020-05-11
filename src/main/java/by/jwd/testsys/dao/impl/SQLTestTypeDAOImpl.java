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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private static final String SELECT_TYPE_WITH_TESTS = "SELECT type.id as tp_id, type.title tp_title, " +
            "test.time as t_time,test.id tt_id, test.title tt_title, test.type_id, count(question.id) as count_quest " +
            "FROM type INNER JOIN test ON test.type_id=type.id inner join question on question.test_id=test.id " +
            "WHERE type.deleted_at IS null AND test.deleted_at IS null AND question.deleted_at is null " +
            "group by test.id;";

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
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl getAll() method",e);
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
        Statement statement=null;
        ResultSet resultSet=null;
        try {
            connection = connectionPool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_TYPE_WITH_TESTS);
            while (resultSet.next()) {
                Type type = buildType(resultSet);
                typesFromDB.add(type);

                int type_id = resultSet.getInt("type_id");
                Test test = buildTest(resultSet);
                for (Type type1 : typesFromDB) {
                    if (type1.getId() == type_id) {
                        type1.setTests(test);
                    }
                }
            }
        }  catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestTypeDAOImpl getTypeWithTests() method.", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestTypeDAOImpl getTypeWithTests() method",e);
            throw new DAOSqlException("SQLException in SQLTestTypeDAOImpl getTypeWithTests() method.", e);

        } finally {
           connectionPool.closeConnection(connection,statement,resultSet);
        }

        return typesFromDB;
    }


    private Type buildType(ResultSet resultSet) throws SQLException {
        int idType = resultSet.getInt("tp_id");
        String titleType = resultSet.getString("tp_title");
        return new Type(idType, titleType);

    }

    private Test buildTest(ResultSet resultSet) throws SQLException {
        int idTest = resultSet.getInt("tt_id");
        String titleTest = resultSet.getString("tt_title");
        LocalTime testDuration=resultSet.getTime("t_time").toLocalTime();
        int countQuestion = resultSet.getInt("count_quest");
        return new Test(idTest, titleTest,testDuration,countQuestion);
    }
}
