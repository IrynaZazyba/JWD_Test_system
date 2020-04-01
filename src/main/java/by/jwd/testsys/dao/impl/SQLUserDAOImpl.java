package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.logic.util.Role;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDAOImpl implements UserDAO {

    private static Logger logger = LogManager.getLogger();
    private ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private static final String SELECT_ALL_USERS = "SELECT id, login,password, first_name, last_name, role.title" +
            " from users INNER JOIN role ON users.role_id=role.id";
    private static final String INSERT_USER = "INSERT INTO users (login, password, first_name, last_name, role_id) " +
            "VALUES (?,?,?,?,?)";
    private static final String SELECT_USER_BY_LOGIN_PASSWORD = "SELECT u.id,u.login,u.password,u.first_name,u.last_name," +
            "role.title FROM users as u INNER JOIN role ON u.role_id=role.id WHERE u.login=? AND u.password=?";
    private static final String SELECT_ROLE_ID = "SELECT id FROM role WHERE title=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT u.id,u.login,u.password,u.first_name,u.last_name," +
            "role.title FROM users as u INNER JOIN role ON u.role_id=role.id WHERE u.login=?";
    private static final String SELECT_USER_BY_ID = "SELECT u.id,u.login,u.password,u.first_name,u.last_name," +
            "role.title FROM users as u INNER JOIN role ON u.role_id=role.id WHERE u.id=?";
    private static final String UPDATE_USER = "UPDATE users SET login=?, password=?, first_name=?, last_name=?," +
            "role_id=? WHERE id=?";

    @Override
    public List<User> getAll() throws DAOException {
        List<User> usersFromDB;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_USERS);

            usersFromDB = new ArrayList<>();
            while (resultSet.next()) {
                usersFromDB.add(parseUserFromDB(resultSet));
            }
        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in getAll() users method", e);
        } finally {
            connectionPool.closeConnection(connection, statement, resultSet);
        }
        return usersFromDB;
    }

    @Override
    public User create(User user) throws DAOException {

        int id_role = getRoleId(user.getRole());

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, id_role);
            preparedStatement.executeUpdate();

            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in save() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKeys);
        }
        return user;
    }

    @Override
    public User getUserByLoginPassword(String login, String password) throws DAOException {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = parseUserFromDB(resultSet);
            }

        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in getUsersByLogin() method", e);
        } finally {
           connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return user;
    }

    private static User parseUserFromDB(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Role role = Role.valueOf(resultSet.getString("title").toUpperCase());
        return new User(id, login, password, firstName, lastName, role);
    }

    private int getRoleId(Role role) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        int roleId = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_ROLE_ID);
            preparedStatement.setString(1, role.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                roleId = resultSet.getInt(1);
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in getRoleId() method", e);

        } finally {
            connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return roleId;
    }

    @Override
    public User getUserByLogin(String userLogin) throws DAOException {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, userLogin);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = parseUserFromDB(resultSet);
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in getUserByLogin() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws DAOException {
        User userById = null;
        Connection connection = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userById = parseUserFromDB(resultSet);
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException in getUserById() method", e);

        } finally {
           connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return userById;
    }

    @Override
    public User updateUser(User user) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement=null;

        User updatedUser = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, getRoleId(user.getRole()));
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
            updatedUser = user;

        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new DAOSqlException("SQLException updateUser() method", e);
        } finally {
            connectionPool.closeConnection(connection,preparedStatement);
        }
        return updatedUser;
    }


}
