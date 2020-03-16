package by.jwd.testsys.dao.factory;

import by.jwd.testsys.dao.exception.DAOFactoryException;
import by.jwd.testsys.dao.factory.impl.SQLDAOFactory;

public class DAOFactoryProvider {
    private static final DAOFactory sqlDaoFactory = SQLDAOFactory.getInstance();

    private DAOFactoryProvider(){}

    public static DAOFactory getFactory(DAOType type) throws DAOFactoryException {
        switch (type) {
            case SQL:
                return sqlDaoFactory;
            default:
                throw new DAOFactoryException("No such DAO factory type.");
        }
    }
}