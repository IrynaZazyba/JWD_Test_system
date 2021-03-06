package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.UserServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;


public class SignIn implements Command {

    private static Logger logger = LogManager.getLogger(SignIn.class);
    private static final String LOCAL_FILE_NAME = "local";
    private static final String LOCAL_FILE_PACKAGE = "local";
    private static final String LOCAL_MESSAGE_INVALID_SIGN_IN = "message.invalid_sign_in";
    private final static String CONTROLLER_ROUTE = "/test?";
    private final static String COMMAND_MAIN_PAGE_TO_URL = "command=main_page";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        HttpSession session = request.getSession();

        try {
            User userByLogin = userService.checkUserCredentials(login, password);

            if (userByLogin.getId() != 0) {
                session.setAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE, userByLogin.getId());
                session.setAttribute(SessionAttributeName.USER_LOGIN_SESSION_ATTRIBUTE, userByLogin.getLogin());
                session.setAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE, userByLogin.getRole());
                response.sendRedirect(request.getContextPath() + CONTROLLER_ROUTE +COMMAND_MAIN_PAGE_TO_URL);
            } else {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(LOCAL_FILE_PACKAGE + File.separator + LOCAL_FILE_NAME);
                request.setAttribute(RequestParameterName.SIGN_IN_ERROR, resourceBundle.getString(LOCAL_MESSAGE_INVALID_SIGN_IN));

                session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
                forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            }

        } catch (UserServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in SignIn command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
