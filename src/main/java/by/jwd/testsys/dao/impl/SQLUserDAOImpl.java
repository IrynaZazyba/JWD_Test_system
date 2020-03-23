package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
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
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

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

        try {
            connection = connectionPool.takeConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);

            usersFromDB = new ArrayList<>();
            while (resultSet.next()) {
                usersFromDB.add(parseUserFromDB(resultSet));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            throw new DAOSqlException("SQLException in getAll() users method", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in return connection", e);
                }
            }
        }
        return usersFromDB;
    }

    @Override
    public boolean save(User user) throws DAOException {

        if (getUserByLogin(user.getLogin()) == null) {
            return false;
        }

        int id_role = getRoleId(user.getRole());
        Connection connection = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, id_role);
            preparedStatement.executeUpdate();
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            throw new DAOSqlException("SQLException in save() method", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection save");
                }
            }
        }
        return true;
    }

    @Override
    public User getUserByLoginPassword(String login, String password) throws DAOException {
        User user = null;
        Connection connection = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = parseUserFromDB(resultSet);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            throw new DAOSqlException("SQLException in getUsersByLogin() method", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection getByLogin");
                }
            }
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
        int roleId = 0;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROLE_ID);
            preparedStatement.setString(1, role.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roleId = resultSet.getInt(1);
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "SQLException in DAO getRoleId() method");
            throw new DAOSqlException("SQLException in getRoleId() method", e);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection getRoleId");
                }
            }
        }
        return roleId;
    }

    public User getUserByLogin(String userLogin) throws DAOException {
        User user = null;
        Connection connection = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, userLogin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = parseUserFromDB(resultSet);
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Couldn't get data from DB, getUserByLogin()");
            throw new DAOSqlException("SQLException in getUserByLogin() method", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection getRoleId");
                }
            }
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws DAOException {
        User userById = null;
        Connection connection = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userById = parseUserFromDB(resultSet);
            }
        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Couldn't get data from DB, getUserById()");
            throw new DAOSqlException("SQLException in getUserById() method", e);

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection getRoleId");
                }
            }
        }
        return userById;
    }

    public User updateUser(User user) throws DAOSqlException {
        Connection connection = null;
        User updatedUser = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5,getRoleId(user.getRole()));
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
            updatedUser = user;

        } catch (ConnectionPoolException | SQLException e) {
            logger.log(Level.ERROR, "Couldn't get data from DB, updateUser");
            throw new DAOSqlException("SQLException updateUser() method", e);
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in close connection getRoleId");
                }
            }
        }
        return updatedUser;
    }

}
