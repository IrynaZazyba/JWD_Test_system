package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.command.front.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WrongRequest implements Command {

    private final static String CONTROLLER_ROUTE = "/test?";
    private final static String COMMAND_ERROR_PAGE_TO_URL = "command=error_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + CONTROLLER_ROUTE + COMMAND_ERROR_PAGE_TO_URL);
    }
}
