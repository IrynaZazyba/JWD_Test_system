package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLTestTypeDAOImpl implements TestTypeDAO {

    private static Logger logger = LogManager.getLogger();
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String SELECT_ALL_TYPES = "SELECT id, title from type WHERE deleted_at IS null";
    private static final String SELECT_TYPE_WITH_TESTS = "SELECT type.id as tp_id, type.title tp_title, test.id tt_id," +
            " test.title tt_title, test.type_id FROM type INNER JOIN test ON test.type_id=type.id " +
            "WHERE type.deleted_at IS null AND test.deleted_at IS null";

    @Override
    public List<Type> getAll() throws DAOException {

        Connection connection;
        List<Type> typesFromDB = null;
        try {
            connection = connectionPool.takeConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_TYPES);
            typesFromDB = new ArrayList<>();
            while (resultSet.next()) {
                int typeId = resultSet.getInt("id");
                String typeTitle = resultSet.getString("title");
                typesFromDB.add(new Type(typeId, typeTitle));
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Exception in SQLTypeDAOImpl getAll().");
            throw new DAOSqlException("Exception in SQLTypeDAOImpl getAll().",e);
        }

        return typesFromDB;
    }

    @Override
    public Set<Type> getTypeWithTests() throws DAOException {
        Connection connection = null;
        Set<Type> typesFromDB = new HashSet<>();

        try {
            connection = connectionPool.takeConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_TYPE_WITH_TESTS);
            while (resultSet.next()) {
                Type type = parseType(resultSet);
                typesFromDB.add(type);

                int type_id = resultSet.getInt("type_id");
                Test test = parseTest(resultSet);
                for (Type type1 : typesFromDB) {
                    if (type1.getId() == type_id) {
                        type1.setTests(test);
                    }
                }

            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "Exception in SQLTypeDAOImpl getTypeWithTests()method.");
            throw new DAOSqlException("Exception in SQLTypeDAOImpl getTypeWithTests() method ", e);
        }

        return typesFromDB;
    }


    private Type parseType(ResultSet resultSet) throws SQLException {
        int idType = resultSet.getInt("tp_id");
        String titleType = resultSet.getString("tp_title");
        return new Type(idType, titleType);

    }

    private Test parseTest(ResultSet resultSet) throws SQLException {
        int idTest = resultSet.getInt("tt_id");
        String titleTest = resultSet.getString("tt_title");
        return new Test(idTest, titleTest);
    }
}
