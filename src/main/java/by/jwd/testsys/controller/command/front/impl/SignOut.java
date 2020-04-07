package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignOut implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_LOGIN_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.COMMAND_NAME);
        response.sendRedirect(JspPageName.START_PAGE);
    }
}
