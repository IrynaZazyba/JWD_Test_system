package by.jwd.testsys.logic.ajaxCommand.impl;

import by.jwd.testsys.logic.ajaxCommand.AjaxCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCommand implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "{\"status\":\"error\"}";
    }
}
