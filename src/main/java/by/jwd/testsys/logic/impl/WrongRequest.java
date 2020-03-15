package by.jwd.testsys.logic.impl;

import by.jwd.testsys.logic.Command;
import by.jwd.testsys.logic.exception.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WrongRequest implements Command {

    private static final String ERROR_URL="jsp/errorPage.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Command.forwardToPage(request,response,ERROR_URL);

    }
}
