package by.jwd.testsys.controller.listener;

import by.jwd.testsys.dao.dbconn.factory.ConnectionPoolFactory;
import by.jwd.testsys.dao.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ConnectionPoolListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger();

    public ConnectionPoolListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPoolFactory.getInstance().getMySqlConnectionPoolDAO().init();
        } catch (DAOException e) {
            logger.log(Level.ERROR, "Connection pool didn't initialize.");
            //todo
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPoolFactory.getInstance().getMySqlConnectionPoolDAO().destroy();
    }
}
