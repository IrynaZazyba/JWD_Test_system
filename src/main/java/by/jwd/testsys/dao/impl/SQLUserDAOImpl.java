package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.bean.Role;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLUserDAOImpl implements UserDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
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

    private static final String SELECT_USER_ASSIGNMENT_BY_USER_ID = "SELECT id, date, deadline, test_id, completed " +
            "FROM assignment where user_id=? AND deleted_at IS NULL";

    private static final String SELECT_USER_ASSIGNMENT_BY_ASSIGNMENT_ID = "SELECT id, date, deadline, test_id, " +
            "completed FROM assignment where id=? AND deleted_at IS NULL";

    private static final String SELECT_USER_ASSIGNMENT_BY_TEST_ID = "SELECT id, date, deadline, test_id, completed " +
            "FROM assignment where user_id=? AND test_id=? AND completed is false AND deleted_at IS NULL";

    private static final String SELECT_USER_WITH_ROLE_USER = "SELECT id, first_name, last_name FROM `users` WHERE " +
            "role_id=(SELECT id from role where title='USER')";

    private static final String INSERT_NEW_ASSIGNMENT = "INSERT INTO `assignment` (`date`, `deadline`, `test_id`, " +
            "`user_id`, `completed`) VALUES (?,?,?,?, false)";

    private static final String SELECT_USERS_WITH_ASSIGNMENT_GENERATED = "SELECT  test.title as t_title, assignment.id as asgn_id, date, " +
            "deadline,users.id as u_id, first_name, last_name, completed FROM `assignment` inner join users " +
            "on users.id=assignment.user_id inner join test on test.id=assignment.test_id WHERE assignment.deleted_at IS NULL " +
            "and test.deleted_at is NULL";


    @Override
    public List<User> getAll() throws DAOSqlException {

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
                usersFromDB.add(buildUser(resultSet));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getAll() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getAll() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getAll() method", e);
        } finally {
            connectionPool.closeConnection(connection, statement, resultSet);
        }
        return usersFromDB;
    }

    @Override
    public User create(User user) throws DAOSqlException {

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
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getAll() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getAll() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getAll() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKeys);
        }
        return user;
    }

    @Override
    public User getUserByLoginPassword(String login, String password) throws DAOSqlException {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserByLoginPassword() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUsersByLogin() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUsersByLogin() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return user;
    }

    private static User buildUser(ResultSet resultSet) throws SQLException {
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int roleId = 0;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_ROLE_ID);
            preparedStatement.setString(1, role.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                roleId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getRoleId() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getRoleId() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getRoleId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return roleId;
    }

    //todo return id user
    @Override
    public User getUserByLogin(String userLogin) throws DAOSqlException {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, userLogin);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserByLogin() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserByLogin() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserByLogin() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws DAOSqlException {
        User userById = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userById = buildUser(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserById() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserById() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserById() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return userById;
    }

    @Override
    public User updateUser(User user) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        User updatedUser;
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

        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl updateUser() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl updateUser() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl updateUser() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return updatedUser;
    }

    @Override
    public Set<Assignment> getUserAssignment(int user_id) throws DAOSqlException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        Set<Assignment> assignments = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_ASSIGNMENT_BY_USER_ID);
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(user_id);
                Assignment assignment = buildAssignment(resultSet);
                assignment.setUser(user);
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserAssignment() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserAssignment() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserAssignment() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return assignments;
    }


    @Override
    public Assignment getUserAssignmentByTestId(int user_id, int test_id) throws DAOSqlException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        Assignment assignment = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_ASSIGNMENT_BY_TEST_ID);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, test_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(user_id);
                assignment = buildAssignment(resultSet);
                assignment.setUser(user);

            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement,resultSet);
        }
        return assignment;
    }

    @Override
    public Assignment getUserAssignmentByAssignmentId(int assignmentId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        Assignment assignment = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_ASSIGNMENT_BY_ASSIGNMENT_ID);
            preparedStatement.setInt(1, assignmentId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                assignment = buildAssignment(resultSet);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserAssignmentByAssignmentId() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserAssignmentByAssignmentId() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserAssignmentByAssignmentId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return assignment;
    }

    @Override
    public Set<User> getUserWithRoleUser() throws DAOSqlException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Set<User> students = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_USER_WITH_ROLE_USER);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(id, firstName, lastName);
                students.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserWithRoleUser() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserWithRoleUser() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl getUserWithRoleUser() method", e);
        } finally {
            connectionPool.closeConnection(connection, statement, resultSet);
        }
        return students;
    }

    @Override
    public void insertNewAssignment(LocalDate assignmentDate, LocalDate deadline, int testId, List<Integer> usersId) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(INSERT_NEW_ASSIGNMENT);
            connection.setAutoCommit(false);
            for (Integer id : usersId) {
                preparedStatement.setDate(1, Date.valueOf(assignmentDate));
                preparedStatement.setDate(2, Date.valueOf(deadline));
                preparedStatement.setInt(3, testId);
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("SQLException in SQLUserDAOImpl insertNewAssignment() method", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLUserDAOImpl method insertNewAssignment()", e);
                throw new DAOSqlException("Impossible to rollback SQLUserDAOImpl method insertNewAssignment()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl insertNewAssignment() method", e);
            throw new DAOSqlException("ConnectionPoolException in SQLUserDAOImpl insertNewAssignment() method", e);
        }finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public Set<User> getUsersWithAssignmentByTestId(int testId, int testTypeId, boolean isCompleted) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<User> users = new HashSet<>();
        String query = SELECT_USERS_WITH_ASSIGNMENT_GENERATED;

        try {
            connection = connectionPool.takeConnection();

            int count = 1;

            if (testId != 0) {
                query = query + " AND test_id=?";
            }

            if (testTypeId != 0) {
                query = query + " AND test.type_id=?";
            }

            if (!isCompleted) {
                query = query + " AND completed=?";
            }


            preparedStatement = connection.prepareStatement(query);


            if (testId != 0) {
                preparedStatement.setInt(count, testId);
                count++;
            }

            if (testTypeId != 0) {
                preparedStatement.setInt(count, testTypeId);
                count++;
            }

            if (!isCompleted) {
                preparedStatement.setBoolean(count, isCompleted);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int user_id = resultSet.getInt("u_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                User user = new User(user_id, first_name, last_name, new HashSet<Assignment>());
                int asgn_id = resultSet.getInt("asgn_id");
                Date date = resultSet.getDate("date");
                Date deadline = resultSet.getDate("deadline");
                boolean completed = resultSet.getBoolean("completed");
                String t_title = resultSet.getString("t_title");
                Test test = new Test(testId, t_title);
                Assignment assignment = new Assignment(asgn_id, date.toLocalDate(), deadline.toLocalDate(), test, completed);
                if (users.add(user)) {
                    user.getAssignment().add(assignment);
                } else {
                    for (User findUser : users) {
                        if (findUser.getId() == user.getId()) {
                            findUser.getAssignment().add(assignment);
                        }
                    }
                }
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return users;
    }

    private Assignment buildAssignment(ResultSet resultSet) throws SQLException {

        int id = resultSet.getInt("id");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        LocalDate deadline = resultSet.getDate("deadline").toLocalDate();
        int testId = resultSet.getInt("test_id");
        Test test = new Test();
        test.setId(testId);
        boolean completed = resultSet.getBoolean("completed");

        return new Assignment(id, date, deadline, test, completed);
    }

}
