package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.CommandName;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ShowUserPage implements Command {


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        ServiceFactory serviceFactory=ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
            User userInfoToAccount = userService.userInfoToAccount(userId);

            req.setAttribute(RequestParameterName.USER_ACCOUNT_INFO, userInfoToAccount);
            session.setAttribute(SessionAttributeName.QUERY_STRING,req.getQueryString());
            forwardToPage(req, resp, JspPageName.JSP_PAGE_PATH);

        } catch (ServiceException | ForwardCommandException e) {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
