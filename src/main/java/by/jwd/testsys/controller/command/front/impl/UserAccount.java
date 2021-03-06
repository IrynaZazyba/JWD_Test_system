package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
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

public class UserAccount implements Command {

    private static Logger logger = LogManager.getLogger(UserAccount.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServiceFactory serviceFactory=ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        HttpSession session = req.getSession();
        int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        try {
            User userInfoToAccount = userService.userInfoToAccount(userId);
            req.setAttribute(RequestParameterName.USER_ACCOUNT_INFO, userInfoToAccount);

            session.setAttribute(SessionAttributeName.QUERY_STRING,req.getQueryString());
            forwardToPage(req, resp, JspPageName.USER_ACCOUNT_PAGE);

        } catch (UserServiceException e) {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR,"Forward to page Exception in ShowUserPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowUserPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
