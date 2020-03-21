package by.jwd.testsys.logic.ajaxCommand.impl;

import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCommand implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Command.forwardToPage(request,response, JspPageName.ERROR_PAGE);

    }
}
