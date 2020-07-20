package by.jwd.testsys.controller.listener;

import by.jwd.testsys.dao.dbconn.ConnectionPoolDAO;
import by.jwd.testsys.dao.dbconn.ConnectionPoolException;
import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger(ConnectionPoolListener.class);

    public ConnectionPoolListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPoolFactory poolFactory = ConnectionPoolFactory.getInstance();
            ConnectionPoolDAO connectionPoolDAO = poolFactory.getSqlConnectionPoolDAO();
            connectionPoolDAO.initPoolData();

        } catch (ConnectionPoolException e) {
            logger.log(Level.ERROR, "Connection pool didn't initialize.",e);
            throw new InitConnectionPoolRuntimeException("Connection pool didn't initialize.",e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPoolFactory poolFactory = ConnectionPoolFactory.getInstance();
        ConnectionPoolDAO connectionPoolDAO = poolFactory.getSqlConnectionPoolDAO();
        connectionPoolDAO.dispose();
    }
}
