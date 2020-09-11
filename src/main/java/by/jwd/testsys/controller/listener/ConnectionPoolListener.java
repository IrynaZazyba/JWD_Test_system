package by.jwd.testsys.controller.listener;

import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.impl.SqlConnectionPoolDAOImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {

    private static Logger logger = LogManager.getLogger(ConnectionPoolListener.class);

    public ConnectionPoolListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPoolDAO connectionPool = SqlConnectionPoolDAOImpl.getInstance();
            connectionPool.initPoolData();

        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "Connection pool didn't initialize.", e);
            throw new InitConnectionPoolRuntimeException("Connection pool didn't initialize.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPoolDAO connectionPool = SqlConnectionPoolDAOImpl.getInstance();
        connectionPool.dispose();
    }
}
