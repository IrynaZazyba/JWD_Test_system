package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.service.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDAOImpl implements UserDAO {

    private ConnectionPool connectionPool = new ConnectionPool();
    private static final String SELECT_ALL_USERS = "SELECT id, login,password, first_name, last_name, role.title" +
            " from users INNER JOIN role ON users.role_id=role.id";
    private static final String INSERT_USER = "INSERT INTO users (login, password, first_name, last_name, role_id) " +
            "VALUES (?,?,?,?,1)";
    private static final String SELECT_USER_BY_LOGIN = "SELECT u.id,u.login,u.password,u.first_name,u.last_name," +
            "role.title FROM users as u INNER JOIN role ON u.role_id=role.id WHERE u.login=?";


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
                usersFromDB.add(getUserFromDB(resultSet));
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
                    //todo
                    throw new DAOSqlException("SQLException in close connection", e);
                }
            }
        }

        return usersFromDB;
    }

    @Override
    public void save(User user) throws DAOException {
        Connection connection = null;
        try {
            connection = connectionPool.takeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
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
                    //todo
                    throw new DAOSqlException("SQLException in close connection", e);
                }
            }
        }
    }

    @Override
    public User getUserByLogin(String login) throws DAOException {
        User user = null;
        Connection connection = null;
        try {
            System.out.println("in getUserBYLogin");
            connection = connectionPool.takeConnection();
            System.out.println("after in getUserBYLogin");

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
               user = getUserFromDB(resultSet);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPool DAO exception", e);
        } catch (SQLException e) {
            throw new DAOSqlException("SQLException in getUsersByLogin() method", e);
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //todo
                    throw new DAOSqlException("SQLException in close connection", e);
                }
            }
        }
        return user;
    }


    private static User getUserFromDB(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        Role role = Role.valueOf(resultSet.getString("title").toUpperCase());
        return new User(id, login, password, firstName, lastName, role);
    }
}
