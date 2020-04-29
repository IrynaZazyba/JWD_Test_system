package by.jwd.testsys.dao.factory;

import by.jwd.testsys.dao.TestDAO;
import by.jwd.testsys.dao.TestLogDAO;
import by.jwd.testsys.dao.TestTypeDAO;
import by.jwd.testsys.dao.UserDAO;

public interface DAOFactory {

    UserDAO getUserDao();
    TestTypeDAO getTypeDao();
    TestDAO getTestDao();

    TestLogDAO getTestLogDao();
}
