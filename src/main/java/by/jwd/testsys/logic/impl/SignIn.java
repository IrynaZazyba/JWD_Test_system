package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.controller.SessionAttrinbuteName;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOType;
import by.jwd.testsys.logic.Command;
import by.jwd.testsys.logic.exception.CommandException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SignIn implements Command {

    private static final String INVALID_PASSWORD_MESSAGE = "Login or password invalid.";
    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);

        DAOFactory daoFactory = Command.getDAOFactory(DAOType.SQL);
        UserDAO userDao = daoFactory.getUserDao();

        try {
            User userByLogin = userDao.getUserByLogin(login);

            if (userByLogin != null && userByLogin.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute(SessionAttrinbuteName.USER_ID_SESSION_ATTRIBUTE, userByLogin.getId());
                session.setAttribute(SessionAttrinbuteName.USER_LOGIN_SESSION_ATTRIBUTE, userByLogin.getLogin());
                session.setAttribute(SessionAttrinbuteName.USER_ROLE_SESSION_ATTRIBUTE, userByLogin.getRole());
                Command.forwardToPage(request, response, JspPageName.START_MENU_PAGE);
            } else {
                request.setAttribute(RequestParameterName.SIGN_IN_ERROR, INVALID_PASSWORD_MESSAGE);
                Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            }
        } catch (DAOException e) {
            logger.log(Level.ERROR, "DAOException in sign in command.");
            Command.forwardToPage(request, response, JspPageName.ERROR_PAGE);

        }
    }
}
