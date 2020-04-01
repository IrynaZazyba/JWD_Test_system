package by.jwd.testsys.logic.core.impl;

import by.jwd.testsys.controller.parameters.SessionAttributeName;
import by.jwd.testsys.logic.core.Command;

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


        String command = (String) session.getAttribute("command");

        if (command != null) {
            response.sendRedirect(request.getContextPath() + "/test?command=" + command);
        } else {
            response.sendRedirect(request.getContextPath());
        }

    }
}
