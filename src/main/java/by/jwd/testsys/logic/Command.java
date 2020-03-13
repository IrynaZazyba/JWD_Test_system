package by.jwd.testsys.logic;


import by.jwd.testsys.logic.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

public interface Command {

   String execute(HttpServletRequest request) throws CommandException;
}
