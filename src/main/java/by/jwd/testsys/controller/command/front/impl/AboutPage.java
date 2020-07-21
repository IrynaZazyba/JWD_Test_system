package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AboutPage implements Command {

    private static Logger logger = LogManager.getLogger(AboutPage.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());

        try {
            forwardToPage(request, response, JspPageName.ABOUT_US_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in AboutPage command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
