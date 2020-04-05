package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCommand implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "{\"status\":\"error\"}";
    }
}
