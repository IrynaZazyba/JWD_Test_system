package by.jwd.testsys.dao.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Role;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLUserDAOImpl implements UserDAO {

    private static Logger logger = LogManager.getLogger();
    private final ConnectionPoolFactory connectionPoolFactory = ConnectionPoolFactory.getInstance();
    private ConnectionPoolDAO connectionPool = connectionPoolFactory.getMySqlConnectionPoolDAO();

    private static final String SELECT_ALL_USERS = "SELECT users.id, login,first_name, last_name, email, role.title" +
            " from users INNER JOIN role ON users.role_id=role.id";
    private static final String INSERT_USER = "INSERT INTO users (login, password, first_name, last_name,email, role_id) " +
            "VALUES (?,?,?,?,?,?)";
    private static final String SELECT_ROLE_ID = "SELECT id FROM role WHERE title=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT users.id,login,password,first_name,last_name," +
            "title, email FROM users INNER JOIN role ON users.role_id=role.id WHERE login=?";
    private static final String SELECT_USER_BY_ID = "SELECT users.id,login,first_name,last_name," +
            "title, email FROM users INNER JOIN role ON users.role_id=role.id WHERE users.id=?";

    private static final String SELECT_PASSWORD_BY_USER_ID = "SELECT password FROM users WHERE id=?";

    private static final String UPDATE_USER = "UPDATE users SET login=?,first_name=?, last_name=?," +
            "role_id=?, email=? WHERE id=?";
    //todo 2 метода для пароля
    private static final String UPDATE_USER_PASSWORD = "UPDATE users SET password=? WHERE id=?";
    private static final String WRITE_ASSIGNMENT = "INSERT INTO `assignment`" +
            " (`date`,`deadline`, `test_id`, `user_id`, `completed`) " +
            "VALUES (?,?,?,?,?)";

    private static final String SELECT_USER_ASSIGNMENT_BY_USER_ID = "SELECT id, date, deadline, test_id, completed " +
            "FROM assignment where user_id=? AND deleted_at IS NULL";

    private static final String SELECT_USER_EMAIL_BY_ID = "SELECT email FROM users where id=?";

    private static final String SELECT_USER_ASSIGNMENT_BY_ASSIGNMENT_ID = "SELECT id, date, deadline, test_id, " +
            "completed FROM assignment where id=? AND deleted_at IS NULL";

    private static final String SELECT_USER_ASSIGNMENT_BY_TEST_ID = "SELECT id, date, deadline, test_id, completed " +
            "FROM assignment where user_id=? AND test_id=? AND completed is false AND deleted_at IS NULL";

    private static final String SELECT_USER_BY_ROLE = "SELECT id, first_name, last_name FROM `users` WHERE " +
            "role_id=(SELECT id from role where title=?)";

    private static final String INSERT_NEW_ASSIGNMENT = "INSERT INTO `assignment` (`date`, `deadline`, `test_id`, " +
            "`user_id`, `completed`) VALUES (?,?,?,?, false)";

    private static final String SELECT_USERS_WITH_ASSIGNMENT_GENERATED = "SELECT  title, " +
            "assignment.id as asgn_id, date, deadline,users.id as u_id, first_name, last_name,email, completed " +
            "FROM `assignment` inner join users on users.id=assignment.user_id inner join test " +
            "on test.id=assignment.test_id WHERE assignment.deleted_at IS NULL and test.deleted_at is NULL";

    private static final String UPDATE_ASSIGNMENT_COMPLETE = "UPDATE `assignment` SET `completed`=? " +
            "WHERE assignment.id=?";

    private static final String UPDATE_ASSIGNMENT_DELETED_AT = "UPDATE `assignment` SET `deleted_at`=? where id=?";


    private static final String ASSIGNMENT_TEST_TITLE_COLUMN = "title";
    private static final String ASSIGNMENT_ID_COLUMN_ALIAS = "asgn_id";
    private static final String USER_ID_COLUMN_ALIAS = "u_id";

    private static final String USER_ID_COLUMN = "id";
    private static final String USER_LOGIN_COLUMN = "login";
    private static final String USER_PASSWORD_COLUMN = "password";
    private static final String USER_FIRST_NAME_COLUMN = "first_name";
    private static final String USER_LAST_NAME_COLUMN = "last_name";
    private static final String USER_EMAIL_COLUMN = "email";
    private static final String ROLE_TITLE_COLUMN = "title";
    private static final String ASSIGNMENT_ID_COLUMN = "id";
    private static final String ASSIGNMENT_DATE_COLUMN = "date";
    private static final String ASSIGNMENT_DEADLINE_COLUMN = "deadline";
    private static final String ASSIGNMENT_TEST_ID_COLUMN = "test_id";
    private static final String ASSIGNMENT_COMPLETED_COLUMN = "completed";


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
                usersFromDB.add(buildUser(resultSet));
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getTypes() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getTypes() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getTypes() method", e);
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
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setInt(6, id_role);
            preparedStatement.executeUpdate();

            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl create() method", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl create() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl create() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKeys);
        }
        return user;
    }

    @Override
    public User getUserByLogin(String userLogin) throws DAOException {
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserByLogin() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws DAOException {
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserById() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return userById;
    }

    @Override
    public String getPassword(int userId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String password = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_PASSWORD_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getPassword() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getPassword() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getPassword() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return password;
    }

    @Override
    public User updateUser(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User updatedUser;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setInt(4, getRoleId(user.getRole()));
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
            updatedUser = user;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl updateUser() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl updateUser() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl updateUser() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
        return updatedUser;
    }


    @Override
    public void updateUserPassword(int userId, String password) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER_PASSWORD);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl updateUser() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl updateUser() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl updateUser() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }


    @Override
    public Set<Assignment> getUserAssignment(int user_id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Assignment> assignments = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_ASSIGNMENT_BY_USER_ID);
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User.Builder().withId(user_id).build();
                Assignment assignment = buildAssignment(resultSet);
                assignment.setUser(user);
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserAssignment() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserAssignment() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserAssignment() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return assignments;
    }


    @Override
    public Assignment getUserAssignmentByTestId(int user_id, int test_id) throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Assignment assignment = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_ASSIGNMENT_BY_TEST_ID);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, test_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User.Builder().withId(user_id).build();
                assignment = buildAssignment(resultSet);
                assignment.setUser(user);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserAssignmentByTestId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return assignment;
    }

    @Override
    public Assignment getUserAssignmentByAssignmentId(int assignmentId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserAssignmentByAssignmentId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return assignment;
    }

    @Override
    public Set<User> getUserByRole(Role role) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<User> students = new HashSet<>();
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ROLE);
            preparedStatement.setString(1, role.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = buildUserWithIdFirstNameLastName(resultSet);
                students.add(user);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl getUserByRole() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl getUserByRole() method", e);
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getUserByRole() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return students;
    }

    @Override
    public void insertAssignment(LocalDate assignmentDate, LocalDate deadline, int testId, List<Integer> usersId) throws DAOException {
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

        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("SQLException in SQLUserDAOImpl insertAssignment() method", e);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.log(Level.ERROR, "Rollback SQLUserDAOImpl method insertAssignment() method", e);
                throw new DAOSqlException("Impossible to rollback SQLUserDAOImpl method insertAssignment()", e);
            }
            logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl insertAssignment() method", e);
            throw new DAOSqlException("SQLException in SQLUserDAOImpl insertAssignment() method", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.log(Level.ERROR, "SQLException in SQLUserDAOImpl method insertAssignment() in" +
                            "attempt to setAutoCommit(true)", e);
                }
            }
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }

    @Override
    public void deleteAssignment(int assignmentId, LocalDate deletedAtDate) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ASSIGNMENT_DELETED_AT);
            preparedStatement.setDate(1, Date.valueOf(deletedAtDate));
            preparedStatement.setInt(2, assignmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method deleteAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method deleteAssignment()", e);
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method deleteAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }


    }

    @Override
    public Set<User> getUsersWithAssignmentByTestId(int testId, int testTypeId, boolean isCompleted) throws DAOException {
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
                int id = resultSet.getInt(USER_ID_COLUMN_ALIAS);
                String firstName = resultSet.getString(USER_FIRST_NAME_COLUMN);
                String lastName = resultSet.getString(USER_LAST_NAME_COLUMN);
                User user = new User.Builder().withId(id).withFirstName(firstName).withLastName(lastName).build();
                user.setAssignment(new HashSet<>());

                String t_title = resultSet.getString(ASSIGNMENT_TEST_TITLE_COLUMN);
                Test test = new Test.Builder().withId(testId).withTitle(t_title).build();

                int asgn_id = resultSet.getInt(ASSIGNMENT_ID_COLUMN_ALIAS);
                Date date = resultSet.getDate(ASSIGNMENT_DATE_COLUMN);
                Date deadline = resultSet.getDate(ASSIGNMENT_DEADLINE_COLUMN);
                boolean completed = resultSet.getBoolean(ASSIGNMENT_COMPLETED_COLUMN);


                Assignment assignment = new Assignment.Builder()
                        .withId(asgn_id)
                        .withAssignmentDate(date.toLocalDate())
                        .withDeadline(deadline.toLocalDate())
                        .withTest(test)
                        .withIsCompleted(completed)
                        .build();

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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getUsersWithAssignmentByTestId()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return users;
    }

    //todo unused
    @Override
    public String getUserEmail(int userId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String email = null;

        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(SELECT_USER_EMAIL_BY_ID);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLTestDAOImpl method getUserEmail()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method getUserEmail()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method getUserEmail()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }

        return email;
    }

    private int getRoleId(Role role) throws DAOException {
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
            throw new DAOConnectionPoolException("ConnectionPoolException in SQLUserDAOImpl getRoleId() method", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, resultSet);
        }
        return roleId;
    }

    @Override
    public void updateAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(UPDATE_ASSIGNMENT_COMPLETE);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, assignmentId);
            preparedStatement.executeUpdate();

        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method updateAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method updateAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method updateAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement);
        }
    }


    @Override
    public Integer writeAssignment(Assignment assignment) throws DAOSqlException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        Integer assignmentId = null;
        try {
            connection = connectionPool.takeConnection();
            preparedStatement = connection.prepareStatement(WRITE_ASSIGNMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, Date.valueOf(assignment.getAssignmentDate()));
            preparedStatement.setDate(2, Date.valueOf(assignment.getDeadline()));
            preparedStatement.setInt(3, assignment.getTest().getId());
            preparedStatement.setInt(4, assignment.getUser().getId());
            preparedStatement.setBoolean(5, false);
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                assignmentId = generatedKeys.getInt(1);
            }
        } catch (ConnectionPoolException e) {
            throw new DAOSqlException("ConnectionPoolException in SQLTestDAOImpl method writeAssignment()", e);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "SQLException in SQLTestDAOImpl method writeAssignment()", e);
            throw new DAOSqlException("SQLException in SQLTestDAOImpl method writeAssignment()", e);
        } finally {
            connectionPool.closeConnection(connection, preparedStatement, generatedKeys);
        }
        return assignmentId;
    }


    private Assignment buildAssignment(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(ASSIGNMENT_ID_COLUMN);
        LocalDate date = resultSet.getDate(ASSIGNMENT_DATE_COLUMN).toLocalDate();
        LocalDate deadline = resultSet.getDate(ASSIGNMENT_DEADLINE_COLUMN).toLocalDate();
        int testId = resultSet.getInt(ASSIGNMENT_TEST_ID_COLUMN);
        Test test = new Test();
        test.setId(testId);
        boolean completed = resultSet.getBoolean(ASSIGNMENT_COMPLETED_COLUMN);
        return new Assignment.Builder()
                .withId(id)
                .withAssignmentDate(date)
                .withDeadline(deadline)
                .withTest(test)
                .withIsCompleted(completed)
                .build();
    }


    private User buildUser(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(USER_ID_COLUMN);
        String login = resultSet.getString(USER_LOGIN_COLUMN);
        String password=resultSet.getString(USER_PASSWORD_COLUMN);
        String firstName = resultSet.getString(USER_FIRST_NAME_COLUMN);
        String lastName = resultSet.getString(USER_LAST_NAME_COLUMN);
        String email = resultSet.getString(USER_EMAIL_COLUMN);
        Role role = Role.valueOf(resultSet.getString(ROLE_TITLE_COLUMN.toUpperCase()));
        return new User.Builder()
                .withId(id)
                .withLogin(login)
                .withPassword(password)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withRole(role)
                .build();
    }

    private User buildUserWithIdFirstNameLastName(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(USER_ID_COLUMN);
        String firstName = resultSet.getString(USER_FIRST_NAME_COLUMN);
        String lastName = resultSet.getString(USER_LAST_NAME_COLUMN);
        return new User.Builder().withId(id).withFirstName(firstName).withLastName(lastName).build();
    }
}
