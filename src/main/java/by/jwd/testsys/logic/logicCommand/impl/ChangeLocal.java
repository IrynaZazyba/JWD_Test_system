package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.logic.logicCommand.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ChangeLocal implements Command {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("local", request.getParameter("local"));
        String command;
        if (session.getAttribute("command") != null) {
            command = (String)session.getAttribute("command");
            response.sendRedirect(request.getContextPath() + "/test?command=" + command);
        } else {
            response.sendRedirect(request.getContextPath());
        }

    }
}
