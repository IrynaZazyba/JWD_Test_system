package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignOut implements Command {

    private final static String CONTROLLER_ROUTE = "/test?";
    private final static String COMMAND_MAIN_PAGE_TO_URL = "command=main_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_LOGIN_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.QUERY_STRING);
        response.sendRedirect(request.getContextPath() + CONTROLLER_ROUTE + COMMAND_MAIN_PAGE_TO_URL);

    }
}
