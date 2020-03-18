package by.jwd.testsys.logic.command.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.controller.SessionAttrinbuteName;
import by.jwd.testsys.logic.command.Command;
import by.jwd.testsys.logic.command.CommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.TestService;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


public class SignIn implements Command {

    private static final String INVALID_PASSWORD_MESSAGE = "Login or password invalid.";
    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);


        UserService userService = ServiceFactory.getInstance().getUserService();
        TestService testService = ServiceFactory.getInstance().getTestService();
        try {
            User userByLogin = userService.getUserByLogin(login);

            if (userByLogin != null && userByLogin.getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute(SessionAttrinbuteName.USER_ID_SESSION_ATTRIBUTE, userByLogin.getId());
                session.setAttribute(SessionAttrinbuteName.USER_LOGIN_SESSION_ATTRIBUTE, userByLogin.getLogin());
                session.setAttribute(SessionAttrinbuteName.USER_ROLE_SESSION_ATTRIBUTE, userByLogin.getRole());


                List<Type> testsType = testService.getAllTestsType();
                request.setAttribute(RequestParameterName.TESTS_TYPE_LIST, testsType);

                Command.forwardToPage(request, response, JspPageName.START_MENU_PAGE);
            } else {
                request.setAttribute(RequestParameterName.SIGN_IN_ERROR, INVALID_PASSWORD_MESSAGE);
                Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            }
        } catch (ServiceException e) {

            logger.log(Level.ERROR, "Service Exception in sign in command.", e);
            Command.forwardToPage(request, response, JspPageName.ERROR_PAGE);

        }
    }
}
