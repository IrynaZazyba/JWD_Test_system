package by.jwd.testsys.logic.service;

import by.jwd.testsys.bean.User;

public interface UserService {

    User getUserByLogin(String userLogin) throws ServiceException;
    boolean addUser(User user) throws ServiceException;

    }
