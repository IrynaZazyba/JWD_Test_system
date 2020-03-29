package by.jwd.testsys.logic.ajaxCommand.impl;

import by.jwd.testsys.logic.ajaxCommand.AjaxCommand;
import by.jwd.testsys.logic.logicCommand.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCommand implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        return "{\"status\":\"error\"}";
    }
}
