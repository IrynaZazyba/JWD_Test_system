package by.jwd.testsys.logic.service.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.exception.DAOSqlException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.UserService;


public class UserServiceImpl implements UserService {

    private DAOFactory daoFactory = DAOFactoryProvider.getSqlDaoFactory();
    private UserDAO userDao = daoFactory.getUserDao();

    public User getUserByLoginPassword(String userLogin, String userPassword) throws ServiceException {

        User userByLogin;
        try {
            userByLogin = userDao.getUserByLoginPassword(userLogin, userPassword);
        } catch (DAOException e) {
            throw new ServiceException("Error in getUserByLoginPassword.", e);
        }
        return userByLogin;
    }

    public boolean addUser(User user) throws ServiceException {
        boolean isSaved;
        try {
            isSaved = userDao.save(user);
        } catch (DAOException e) {
            throw new ServiceException("Error in save user.", e);
        }
        return isSaved;
    }

    public User getUserInfoToAccount(int id) throws ServiceException {
        User userFromDB;
        try {
            userFromDB = userDao.getUserById(id);
        } catch (DAOException e) {
            throw new ServiceException("Error in getUserById().", e);
        }
        return userFromDB;
    }

    public User editUserInfo(User user) throws ServiceException {
        User updatedUser;

        try {
            updatedUser = userDao.updateUser(user);
        } catch (DAOSqlException e) {
            throw new ServiceException("Error in editUserInfo().", e);
        }
        return updatedUser;
    }
}
