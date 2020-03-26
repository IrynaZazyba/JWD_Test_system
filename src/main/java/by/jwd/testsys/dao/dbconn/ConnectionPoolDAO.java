package by.jwd.testsys.dao.dbconn;

import by.jwd.testsys.dao.exception.DAOException;

public interface ConnectionPoolDAO {

    void init() throws DAOException;

    void destroy();
}
