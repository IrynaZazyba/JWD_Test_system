package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import by.jwd.testsys.bean.Role;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;


public class SignUp implements Command {
    private static Logger logger = LogManager.getLogger();

    //todo synchronized чтоб два не записались одновременно
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(RequestParameterName.USER_EMAIL_PARAMETER);
        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String firstName = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String lastName = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);

        HttpSession session = request.getSession();
        UserService userService = ServiceFactory.getInstance().getUserService();

        Set<String> validateResult = userService.validateUserData(login, password, firstName, lastName,email);
        checkAnswerAccordingValidation(validateResult, request, response);


        if (validateResult.size() == 0) {

            try {

                User user = new User.Builder()
                        .withLogin(login)
                        .withPassword(password)
                        .withFirstName(firstName)
                        .withLastName(lastName)
                        .withEmail(email)
                        .withRole(Role.USER).build();
                userService.registerUser(user);

                request.setAttribute(RequestParameterName.SIGN_UP_SUCCESS_MESSAGE,RequestParameterName.SIGN_UP_SUCCESS_MESSAGE);
                forwardToPage(request, response, JspPageName.START_JSP_PAGE);


            } catch (ServiceException | ForwardCommandException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
                //todo
                response.sendRedirect(JspPageName.ERROR_PAGE);
            } catch (ExistsUserException ex) {

                request.setAttribute(RequestParameterName.SIGN_UP_ERROR,RequestParameterName.SIGN_UP_ERROR);
                request.setAttribute(RequestParameterName.SIGN_UP_EXISTS_ERROR,RequestParameterName.SIGN_UP_EXISTS_ERROR);

                try {
                    forwardToPage(request, response, JspPageName.START_JSP_PAGE);
                } catch (ForwardCommandException e) {
                    logger.log(Level.ERROR,"Forward to page Exception in SignUp command", ex);
                    response.sendRedirect(JspPageName.ERROR_PAGE);
                }

            }

        }
    }

    private void checkAnswerAccordingValidation(Set<String> userValidateAnswer,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws ServletException, IOException {

        if (userValidateAnswer != null & userValidateAnswer.size() != 0) {
            userValidateAnswer.forEach((k) -> request.setAttribute(k.toLowerCase(),k));
            request.setAttribute(RequestParameterName.SIGN_UP_ERROR, "error");
            try {
                forwardToPage(request, response, JspPageName.START_JSP_PAGE);
            } catch (ForwardCommandException e) {
                logger.log(Level.ERROR, e.getMessage(), e);
                response.sendRedirect(JspPageName.ERROR_PAGE);
            }
        }

    }
}
