package by.jwd.testsys.logic.service;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.logic.service.exception.ExistsUserException;
import by.jwd.testsys.logic.service.exception.ServiceException;

public interface UserService {

    User getUserByLoginPassword(String userLogin, String userPassword) throws ServiceException;

    User registerUser(User user) throws ServiceException,ExistsUserException;

    User getUserInfoToAccount(int id) throws ServiceException;

    User editUserInfo(User user) throws ServiceException;

}
