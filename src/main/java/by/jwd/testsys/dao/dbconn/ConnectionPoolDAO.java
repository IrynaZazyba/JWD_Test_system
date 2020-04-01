package by.jwd.testsys.dao.dbconn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public interface ConnectionPoolDAO {

    void initPoolData() throws ConnectionPoolException;
    void dispose();
    Connection takeConnection() throws ConnectionPoolException;
    void closeConnection(Connection con, Statement stm, ResultSet resultSet);
    void closeConnection(Connection con, Statement stm);

}
