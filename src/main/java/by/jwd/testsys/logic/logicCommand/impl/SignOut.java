package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.SessionAttributeName;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignOut implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)throws CommandException {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_LOGIN_SESSION_ATTRIBUTE);
        session.removeAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE);
        Command.redirectToPage(response, JspPageName.START_PAGE);
    }
}
