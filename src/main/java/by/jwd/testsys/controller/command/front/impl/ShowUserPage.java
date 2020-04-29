package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.CommandName;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.TestService;
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

public class ShowUserPage implements Command {

    private static Logger logger = LogManager.getLogger();
    private static final String JSP_PAGE_PATH = "WEB-INF/jsp/userAccount.jsp";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserService userService = ServiceFactory.getInstance().getUserService();
        HttpSession session = req.getSession();


        ServiceFactory serviceFactory=ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();


        try {
            int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
            User userInfoToAccount = userService.userInfoToAccount(userId);

            req.setAttribute(RequestParameterName.USER_ACCOUNT_INFO, userInfoToAccount);
            session.setAttribute(SessionAttributeName.COMMAND_NAME, CommandName.SHOW_USER_ACCOUNT.toString());
            forwardToPage(req, resp, JSP_PAGE_PATH);

        } catch (ServiceException | ForwardCommandException e) {
            logger.log(Level.ERROR, e.getMessage(),e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
