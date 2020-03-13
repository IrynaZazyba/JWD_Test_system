package by.jwd.testsys.dao.factory;

import by.jwd.testsys.dao.factory.impl.SQLDAOFactory;

public class DAOFactoryProvider {
    private static final DAOFactory sqlDaoFactory = new SQLDAOFactory();

    public static DAOFactory getFactory(DAOType type) {
        if (type == DAOType.SQL) {
            return sqlDaoFactory;
        }

        return null;
    }
}
