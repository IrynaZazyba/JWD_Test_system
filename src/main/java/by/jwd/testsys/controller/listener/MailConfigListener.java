package by.jwd.testsys.controller.listener;

import by.jwd.testsys.logic.util.MailSender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class MailConfigListener implements ServletContextListener {

    private Logger logger = LogManager.getLogger(MailConfigListener.class);
    private static final String MAIL_PROPERTIES = "mail.properties";

    public MailConfigListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream(MAIL_PROPERTIES);
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                MailSender instance = MailSender.getInstance();
                instance.initializeSalSender(properties);
            }

        } catch (IOException e) {
            logger.log(Level.ERROR,"Exception in MailConfigListener in attempt to load mail.properties" ,e);
            throw new InitMailConfigRuntimeException("MailConfigListener didn't initialize.",e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
