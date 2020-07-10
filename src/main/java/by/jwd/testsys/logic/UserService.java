package by.jwd.testsys.logic;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.exception.UserServiceException;

import java.util.Set;


public interface UserService {

    User checkUserCredentials(String userLogin, String userPassword) throws UserServiceException;

    User registerUser(User user) throws UserServiceException, ExistsUserException;

    User userInfoToAccount(int id) throws UserServiceException, InvalidUserDataException;

    User editUserInfo(User user) throws UserServiceException, InvalidUserDataException;

    Set<User> getStudents() throws UserServiceException;

    Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws UserServiceException, InvalidUserDataException;

    Set<String> validateUserData(String login, String password, String firstName, String lastName, String email);

    void changePassword(int userId,String oldPassword, String newPassword) throws UserServiceException,InvalidUserDataException;
}
