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
import by.jwd.testsys.logic.util.Role;
import by.jwd.testsys.logic.util.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SignUp implements Command {
    private Logger logger = LogManager.getLogger();

    private static final String ALREADY_EXISTS_LOGIN_MESSAGE = "Choose another login. Such login already exist!";
    private static final String INVALID_LOGIN_MESSAGE = "Your login is invalid! Please try one more";
    private static final String INVALID_PASSWORD_MESSAGE = "Your password is invalid. The password must contain 6-18" +
            " characters: upper and lower case letters, numbers, dashes and underscores!";
    private static final String INVALID_NAME_MESSAGE = "Name is too long or contains numbers.";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String firstName = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String lastName = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);


        String userDataValid = isUserDataValid(login, password, firstName, lastName);
        if (userDataValid != null) {
            request.setAttribute(RequestParameterName.SIGN_UP_ERROR, userDataValid);
            Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
        } else {

            DAOFactory daoFactory = Command.getDAOFactory(DAOType.SQL);
            UserDAO userDao = daoFactory.getUserDao();

            try {
                User user = new User(login, password, firstName, lastName, Role.USER);
                boolean isSaved = userDao.save(user);

                if (isSaved) {
                    HttpSession session = request.getSession();
                    session.setAttribute(SessionAttrinbuteName.USER_ID_SESSION_ATTRIBUTE, user.getId());
                    session.setAttribute(SessionAttrinbuteName.USER_LOGIN_SESSION_ATTRIBUTE, user.getLogin());
                    session.setAttribute(SessionAttrinbuteName.USER_ROLE_SESSION_ATTRIBUTE, user.getRole());
                    Command.forwardToPage(request, response, JspPageName.START_MENU_PAGE);
                } else {
                    request.setAttribute(RequestParameterName.SIGN_UP_ERROR, ALREADY_EXISTS_LOGIN_MESSAGE);
                    Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
                }
            } catch (DAOException e) {
                logger.log(Level.ERROR, "DAOException in SignUp command.");
                Command.forwardToPage(request, response, JspPageName.ERROR_PAGE);
            }
        }
    }


    private String isUserDataValid(String login, String password, String firstName, String lastName) {
        String message = null;
        if (!UserValidator.validateLogin(login)) {
            message = INVALID_LOGIN_MESSAGE;
        } else if (!UserValidator.validatePassword(password)) {
            message = INVALID_PASSWORD_MESSAGE;
        } else if (!UserValidator.validateName(firstName) || !UserValidator.validateName(lastName)) {
            message = INVALID_NAME_MESSAGE;
        }
        return message;
    }
}
