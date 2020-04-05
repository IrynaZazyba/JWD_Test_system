package by.jwd.testsys.logic;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;


public interface UserService {

    User userByLoginPassword(String userLogin, String userPassword) throws ServiceException;

    User registerUser(User user) throws ServiceException, ExistsUserException;

    User userInfoToAccount(int id) throws ServiceException;

    User editUserInfo(User user) throws ServiceException, InvalidUserDataException;

}
