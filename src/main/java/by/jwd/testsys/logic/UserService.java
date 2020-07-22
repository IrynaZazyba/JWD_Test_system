package by.jwd.testsys.logic;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.exception.*;

import java.util.Set;


public interface UserService {

    User checkUserCredentials(String userLogin, String userPassword) throws UserServiceException;

    User registerUser(User user) throws UserServiceException, ExistsUserException, InvalidUserDataException, ExistsEmailException;

    void sendActivationLetter(String link, String activationCodeParamName, User user) throws UserServiceException;

    User userInfoToAccount(int id) throws UserServiceException, InvalidUserDataException;

    User editUserInfo(User user) throws UserServiceException, InvalidUserDataException, ExistsUserException;

    Set<User> getUsers() throws UserServiceException;

    Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws UserServiceException, InvalidUserDataException;

    Set<String> validateUserData(String login, String password, String firstName, String lastName, String email);

    void changePassword(int userId,String oldPassword, String newPassword) throws UserServiceException,InvalidUserDataException;

    boolean checkRegistration(int userId, String activatedCode) throws UserServiceException, InvalidUserDataException;

    boolean checkAccountStatus(int userId) throws InvalidUserDataException, UserServiceException;

    void markAccountActive(int userId) throws UserServiceException;
}
