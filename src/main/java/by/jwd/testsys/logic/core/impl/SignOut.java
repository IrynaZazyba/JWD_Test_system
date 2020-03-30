package by.jwd.testsys.logic.core.impl;

import by.jwd.testsys.controller.parameters.JspPageName;
import by.jwd.testsys.controller.parameters.SessionAttributeName;
import by.jwd.testsys.logic.core.Command;

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
        response.sendRedirect(JspPageName.START_PAGE);
    }
}
