package by.jwd.testsys.logic.impl;

import by.jwd.testsys.logic.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignOut implements Command {

    private static final String REDIRECT_START_URL="/test-system";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();

        Command.redirectToPage(response,REDIRECT_START_URL);
    }
}
