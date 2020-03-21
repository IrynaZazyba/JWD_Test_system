package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.controller.SessionAttributeName;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ShowUserPage implements Command {

    private static final String JSP_PAGE_PATH = "WEB-INF/jsp/userAccount.jsp";


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {

        UserService userService = ServiceFactory.getInstance().getUserService();

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(JSP_PAGE_PATH);
        HttpSession session = req.getSession(false);

        if (requestDispatcher != null && session != null) {
            try {
                User userInfoToAccount = userService.getUserInfoToAccount((Integer) session.
                        getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE));

                req.setAttribute(RequestParameterName.USER_ACCOUNT_INFO, userInfoToAccount);

                requestDispatcher.forward(req, resp);

            } catch (ServletException | IOException e) {
                throw new CommandException("Command exception ShowMainPage.", e);
            } catch (ServiceException e) {
                //todo
                e.printStackTrace();
            }

        }else {
            Command.forwardToPage(req, resp, JspPageName.ERROR_PAGE);
        }
    }
}
