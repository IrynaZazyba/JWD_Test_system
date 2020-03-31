package by.jwd.testsys.logic.core.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameters.JspPageName;
import by.jwd.testsys.controller.parameters.RequestParameterName;
import by.jwd.testsys.controller.parameters.SessionAttributeName;
import by.jwd.testsys.logic.core.Command;
import by.jwd.testsys.logic.core.CommandName;
import by.jwd.testsys.logic.core.ForwardCommandException;
import by.jwd.testsys.logic.service.exception.ServiceException;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ShowUserPage implements Command {

    private Logger logger = LogManager.getLogger();
    private static final String JSP_PAGE_PATH = "WEB-INF/jsp/userAccount.jsp";

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserService userService = ServiceFactory.getInstance().getUserService();
        HttpSession session = req.getSession();

        try {
            int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
            User userInfoToAccount = userService.getUserInfoToAccount(userId);

            req.setAttribute(RequestParameterName.USER_ACCOUNT_INFO, userInfoToAccount);
            session.setAttribute(SessionAttributeName.COMMAND_NAME, CommandName.SHOW_USER_ACCOUNT);
            forwardToPage(req, resp, JSP_PAGE_PATH);

        } catch (ServiceException | ForwardCommandException e) {
            logger.log(Level.ERROR, e.getMessage());
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
