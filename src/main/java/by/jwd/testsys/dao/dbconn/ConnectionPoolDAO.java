package by.jwd.testsys.dao.dbconn;

import java.sql.Connection;

public interface ConnectionPoolDAO {

    void initPoolData() throws ConnectionPoolException;
    void dispose();
    Connection takeConnection() throws ConnectionPoolException;
}
