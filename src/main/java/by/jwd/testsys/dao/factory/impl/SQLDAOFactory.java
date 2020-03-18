package by.jwd.testsys.dao.factory.impl;

import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.impl.SQLTestTypeDAOImpl;
import by.jwd.testsys.dao.impl.SQLUserDAOImpl;

public class SQLDAOFactory implements DAOFactory {

    private final static SQLDAOFactory sqlDAOFactory = new SQLDAOFactory();
    private final static UserDAO userDao = new SQLUserDAOImpl();
    private final static TestTypeDAO typeDao = new SQLTestTypeDAOImpl();

    private SQLDAOFactory() {
    }

    public static SQLDAOFactory getInstance() {
        return sqlDAOFactory;
    }

    @Override
    public UserDAO getUserDao() {
        return userDao;
    }

    @Override
    public TestTypeDAO getTypeDao() {
        return typeDao;
    }
}
