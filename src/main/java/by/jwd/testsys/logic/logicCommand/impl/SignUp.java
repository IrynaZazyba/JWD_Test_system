package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import by.jwd.testsys.logic.util.Role;
import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class SignUp implements Command {
    private Logger logger = LogManager.getLogger();

    private static final String ALREADY_EXISTS_LOGIN_MESSAGE = "Choose another login. Such login already exist!";
    private static final String SUCCESS_SIGNUP_MESSAGE = "Sign up was successful! Please sign in";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String firstName = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String lastName = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);

        UserValidatorImpl userValidator = new UserValidatorImpl(new User(login, password, firstName, lastName));
        Map<String, String> userValidateAnswer = userValidator.validate();


        if (userValidateAnswer.size()!=0) {
            userValidateAnswer.forEach((k, v) -> request.setAttribute(k, v));
            request.setAttribute(RequestParameterName.SIGN_UP_ERROR, "error");
            Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
        } else {

            UserService userService = ServiceFactory.getInstance().getUserService();

            try {
                User user = new User(login, password, firstName, lastName, Role.USER);
                boolean isSaved = userService.addUser(user);

                if (isSaved) {
                    request.setAttribute(RequestParameterName.SIGN_UP_SUCCESS_MESSAGE, SUCCESS_SIGNUP_MESSAGE);
                    Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
                } else {
                    request.setAttribute(RequestParameterName.SIGN_UP_ERROR, ALREADY_EXISTS_LOGIN_MESSAGE);
                    Command.forwardToPage(request, response, JspPageName.START_JSP_PAGE);
                }
            } catch (ServiceException e) {
                logger.log(Level.ERROR, "Service Exception in SignUp logicCommand.", e);
                Command.forwardToPage(request, response, JspPageName.ERROR_PAGE);
            }
        }
    }

}
