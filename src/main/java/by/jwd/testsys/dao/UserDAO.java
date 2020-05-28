package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UserDAO {

    List<User> getAll() throws DAOSqlException;

    User create(User user) throws DAOSqlException;

    User getUserByLoginPassword(String login, String password) throws DAOSqlException;

    User getUserById(int id) throws DAOSqlException;

    User updateUser(User user) throws DAOSqlException;

    User getUserByLogin(String login) throws DAOException;

    Set<Assignment> getUserAssignment(int user_id) throws DAOSqlException;

    Assignment getUserAssignmentByTestId(int user_id, int test_id) throws DAOSqlException;

    Assignment getUserAssignmentByAssignmentId(int assignmentId) throws DAOSqlException;

    Set<User> getUserWithRoleUser() throws DAOSqlException;

    void insertNewAssignment(LocalDate assignmentDate, LocalDate deadline, int testId, List<Integer> usersId) throws DAOSqlException;

    Set<User> getUsersWithAssignmentByTestId(int testId, boolean isCompleted) throws DAOSqlException;

}