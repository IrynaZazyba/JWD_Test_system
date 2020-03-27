package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.logic.logicCommand.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WrongRequest implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPageName.ERROR_PAGE).forward(request, response);
    }
}
