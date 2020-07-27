package by.jwd.testsys.dao;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Role;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UserDAO {

    Set<User> getAll() throws DAOException;

    User create(User user) throws DAOException;

    User getUserById(int id) throws DAOException;

    String getPassword(int userId) throws DAOException;

    User updateUser(User user) throws DAOException;

    void updateUserIsActivated(int userId, boolean isActivated) throws DAOException;

    User getUserByLogin(String login) throws DAOException;

    Assignment getUserAssignmentByTestId(int user_id, int test_id) throws DAOException;

    Assignment getUserAssignmentByAssignmentId(int assignmentId) throws DAOException;

    void insertAssignment(LocalDate assignmentDate, LocalDate deadline, int testId, List<Integer> usersId) throws DAOException;

    void deleteAssignment(int assignmentId, LocalDate deletedAtDate) throws DAOSqlException;

    Set<User> getUsersWithAssignmentByTestId(int testId, int testTypeId, boolean isCompleted) throws DAOException;

    int getUserIdByEmail(String email) throws DAOException;

    void updateAssignment(int assignmentId, boolean isCompleted) throws DAOSqlException;

    String getActivationCode(int userId) throws DAOSqlException;

    boolean getUserIsActivated(int userId) throws DAOSqlException;

    Integer writeAssignment(Assignment assignment) throws DAOSqlException;

    void updateUserPassword(int userId, String password) throws DAOException;

}