package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

        HttpSession session = request.getSession();
        session.setAttribute("local", request.getParameter("local"));
        System.out.println(request.getParameter("local"));
        String command = (String) session.getAttribute("command");
        System.out.println(command);

        try {
            response.sendRedirect( request.getContextPath()+"/test?command=" + command);
        } catch (IOException e) {
            //todo
            e.printStackTrace();
        }


    }
}
