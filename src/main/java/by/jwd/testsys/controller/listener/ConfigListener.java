package by.jwd.testsys.controller.listener;

import by.jwd.testsys.logic.util.SslSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.util.Properties;

@WebListener
public class ConfigListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger();

    public ConfigListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("mail.properties");
            Properties properties = new Properties();
            properties.load(input);

            SslSender instance = SslSender.getInstance();
            instance.initializeSalSender(properties);
        } catch (IOException e) {
            //todo
            e.printStackTrace();
        }

        // logger.log(Level.ERROR, e.getMessage());
        //throw new InitConnectionPoolRuntimeException("Connection pool didn't initialize.",e);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }



}
