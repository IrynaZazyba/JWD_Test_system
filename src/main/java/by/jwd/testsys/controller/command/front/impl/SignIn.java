package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;


public class SignIn implements Command {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        HttpSession session = request.getSession();

        try {
            User userByLogin = userService.userByLoginPassword(login, password);

            if (userByLogin != null) {
                session.setAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE, userByLogin.getId());
                session.setAttribute(SessionAttributeName.USER_LOGIN_SESSION_ATTRIBUTE, userByLogin.getLogin());
                session.setAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE, userByLogin.getRole());
                response.sendRedirect(request.getContextPath());
            } else {

                ResourceBundle resourceBundle = ResourceBundle.getBundle("local/local");
                request.setAttribute(RequestParameterName.SIGN_IN_ERROR, resourceBundle.getString("message.invalid_sign_in"));
                forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            }

        } catch (ServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR,"Forward to page Exception in SignIn command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
