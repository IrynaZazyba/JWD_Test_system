package by.jwd.testsys.logic;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;

import java.time.LocalDate;
import java.util.Set;


public interface UserService {

    User userByLoginPassword(String userLogin, String userPassword) throws ServiceException;

    User registerUser(User user) throws ServiceException, ExistsUserException;

    User userInfoToAccount(int id) throws ServiceException;

    User editUserInfo(User user) throws ServiceException, InvalidUserDataException;

    Set<User> getStudents() throws ServiceException;

    Set<User> getUsersWithAssignment(int testId, int testTypeId, boolean isCompleted) throws ServiceException;
}
