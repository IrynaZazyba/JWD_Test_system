package by.jwd.testsys.controller.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Listener on a session creation.
 * On session creation will be set a default language.
 */

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final String SESSION_LOCAL = "local";
    private static final String DEFAULT_LANGUAGE_ID = "en";

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute(SESSION_LOCAL, DEFAULT_LANGUAGE_ID);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
