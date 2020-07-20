package by.jwd.testsys.dao.dbconn.factory;


import by.jwd.testsys.dao.dbconn.impl.SqlConnectionPoolDAOImpl;

public final class ConnectionPoolFactory {

    private static final ConnectionPoolFactory instance = new ConnectionPoolFactory();

    private final SqlConnectionPoolDAOImpl SqlConnectionPoolDAO = new SqlConnectionPoolDAOImpl();

    private ConnectionPoolFactory() {
    }

    public static ConnectionPoolFactory getInstance() {
        return instance;
    }

    public SqlConnectionPoolDAOImpl getSqlConnectionPoolDAO() {
        return SqlConnectionPoolDAO;
    }

}
