package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ChangeLocal implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String local = request.getParameter(SessionAttributeName.LANGUAGE_ATTRIBUTE);
        session.setAttribute(SessionAttributeName.LANGUAGE_ATTRIBUTE, local);


        String queryString = (String) session.getAttribute(SessionAttributeName.QUERY_STRING);

        if (queryString != null) {
            response.sendRedirect(request.getContextPath() + "/test?"+queryString);
        } else {
            response.sendRedirect(request.getContextPath());
        }

    }
}
