package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.UserServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ConfirmEmail implements Command {

    private static Logger logger = LogManager.getLogger(ConfirmEmail.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter(RequestParameterName.USER_ID));
        String confirmEmailCode = request.getParameter(RequestParameterName.CONFIRM_EMAIL_CODE);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();
        HttpSession session = request.getSession();

        try {

            boolean accountStatus = userService.checkAccountStatus(userId);
            if (accountStatus) {
                request.setAttribute(RequestParameterName.INVALID_CONFIRM_DATA, RequestParameterName.INVALID_CONFIRM_DATA);
            } else {

                boolean registration = userService.checkRegistration(userId, confirmEmailCode);

                if (!registration) {
                    request.setAttribute(RequestParameterName.INVALID_CONFIRM_DATA, RequestParameterName.INVALID_CONFIRM_DATA);
                } else {
                    userService.markAccountActive(userId);
                    request.setAttribute(RequestParameterName.SUCCESS_CONFIRM_DATA, RequestParameterName.SUCCESS_CONFIRM_DATA);
                }
            }

            forwardToPage(request, response, JspPageName.CONFIRM_EMAIL_PAGE);

        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserDAta Exception in ConfirmEmail command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (UserServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ConfirmEmail command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
