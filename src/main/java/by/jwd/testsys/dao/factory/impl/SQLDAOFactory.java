package by.jwd.testsys.dao.factory.impl;

import by.jwd.testsys.dao.*;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.impl.*;

public class SQLDAOFactory implements DAOFactory {

    private final static SQLDAOFactory sqlDAOFactory = new SQLDAOFactory();
    private final static UserDAO userDao = new SQLUserDAOImpl();
    private final static TestTypeDAO typeDao = new SQLTestTypeDAOImpl();
    private final static TestDAO testDao = new SQLTestDAOImpl();
    private final static TestLogDAO testLogDao = new SQLTestLogDAOImpl();
    private final static TestResultDAO testResultDao = new SQLTestResultDAOImpl();


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

    @Override
    public TestDAO getTestDao() {
        return testDao;
    }

    @Override
    public TestLogDAO getTestLogDao() {
        return testLogDao;
    }

    @Override
    public TestResultDAO getTestResultDao() {
        return testResultDao;
    }

}
