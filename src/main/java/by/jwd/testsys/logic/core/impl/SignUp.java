package by.jwd.testsys.logic.core.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.logic.core.Command;
import by.jwd.testsys.logic.core.ForwardCommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import by.jwd.testsys.logic.util.Role;
import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;


public class SignUp implements Command {
    private Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String firstName = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String lastName = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);

        HttpSession session = request.getSession();
        String locale = null;
        if (session != null) {
            locale = (String) session.getAttribute("local");
        }

        UserValidatorImpl userValidator = new UserValidatorImpl(new User(login, password, firstName, lastName), locale);
        Map<String, String> userValidateAnswer = userValidator.validate();

        viewPageWithError(userValidateAnswer, request, response);

        if (userValidateAnswer.size() == 0) {

            UserService userService = ServiceFactory.getInstance().getUserService();
            try {
                User user = new User(login, password, firstName, lastName, Role.USER);
                boolean isSaved = userService.addUser(user);


                ResourceBundle resourceBundle = ResourceBundle.getBundle("local/local");
                if (isSaved) {
                    request.setAttribute(RequestParameterName.SIGN_UP_SUCCESS_MESSAGE,
                            resourceBundle.getString("message.success_sign_up"));
                    forwardToPage(request, response, JspPageName.START_JSP_PAGE);

                } else {
                    request.setAttribute(RequestParameterName.SIGN_UP_ERROR,
                            resourceBundle.getString("message.exists_login"));
                    forwardToPage(request, response, JspPageName.START_JSP_PAGE);

                }

            } catch (ServiceException | ForwardCommandException e) {
                logger.log(Level.ERROR, e.getMessage());
                response.sendRedirect(JspPageName.ERROR_PAGE);
            }
        }
    }


    private void viewPageWithError(Map<String, String> userValidateAnswer, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (userValidateAnswer.size() != 0) {
            userValidateAnswer.forEach((k, v) -> request.setAttribute(k, v));
            request.setAttribute(RequestParameterName.SIGN_UP_ERROR, "error");
            try {
                forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            } catch (ForwardCommandException e) {
                logger.log(Level.ERROR, e.getMessage());
                response.sendRedirect(JspPageName.ERROR_PAGE);
            }
        }
    }

}
