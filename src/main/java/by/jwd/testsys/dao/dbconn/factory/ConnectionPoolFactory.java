package by.jwd.testsys.dao.dbconn.factory;

import by.jwd.testsys.dao.dbconn.impl.MySqlConnectionPoolDAOImpl;

public final class ConnectionPoolFactory {

    private static final ConnectionPoolFactory instance = new ConnectionPoolFactory();

    private final MySqlConnectionPoolDAOImpl mySqlConnectionPoolDAO = new MySqlConnectionPoolDAOImpl();

    private ConnectionPoolFactory() {
    }

    public static ConnectionPoolFactory getInstance() {
        return instance;
    }

    public MySqlConnectionPoolDAOImpl getMySqlConnectionPoolDAO() {
        return mySqlConnectionPoolDAO;
    }


}
