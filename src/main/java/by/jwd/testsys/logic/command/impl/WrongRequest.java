package by.jwd.testsys.logic.command.impl;

import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.logic.command.Command;
import by.jwd.testsys.logic.command.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WrongRequest implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Command.forwardToPage(request,response, JspPageName.ERROR_PAGE);

    }
}
