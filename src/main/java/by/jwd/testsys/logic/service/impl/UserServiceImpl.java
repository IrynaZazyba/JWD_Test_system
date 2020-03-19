package by.jwd.testsys.logic.service.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
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
            throw new ServiceException("Error in save user in DB.",e);
        }
        return isSaved;
    }

}
