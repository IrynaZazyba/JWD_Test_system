package by.jwd.testsys.dao.factory.impl;

import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.impl.SQLUserDAOImpl;

public class SQLDAOFactory implements DAOFactory {

    private final static UserDAO userDao = new SQLUserDAOImpl();

    public SQLDAOFactory() {
    }

    @Override
    public UserDAO getUserDao() {
        return userDao;
    }


}
