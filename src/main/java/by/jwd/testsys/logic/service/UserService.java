package by.jwd.testsys.logic.service;

import by.jwd.testsys.bean.User;

public interface UserService {

    User getUserByLoginPassword(String userLogin, String userPassword) throws ServiceException;

    boolean addUser(User user) throws ServiceException;

    User getUserInfoToAccount(int id) throws ServiceException;

    User editUserInfo(User user) throws ServiceException;

}
