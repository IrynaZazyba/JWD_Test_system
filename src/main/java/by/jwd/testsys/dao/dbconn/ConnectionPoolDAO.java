package by.jwd.testsys.dao.dbconn;

public interface ConnectionPoolDAO {

    void initPoolData() throws ConnectionPoolException;

    void dispose();
}
