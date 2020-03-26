package by.jwd.testsys.dao.dbconn.impl;

import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOConnectionPoolException;
import by.jwd.testsys.dao.exception.DAOException;

public class MySqlConnectionPoolDAOImpl implements ConnectionPoolDAO {

   public MySqlConnectionPoolDAOImpl() {
    }

    @Override
    public void init() throws DAOException {
        try {
            ConnectionPool.getInstance().initPoolData();
        } catch (ConnectionPoolException e) {
            throw new DAOConnectionPoolException("Impossible to initialise pool", e);
        }

    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().dispose();
    }
}
