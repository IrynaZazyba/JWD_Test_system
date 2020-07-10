package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ShowAssignedTestsPage implements Command {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        int userId = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        try {

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestService testService = serviceFactory.getTestService();

            Set<Test> userAssignedTests = testService.getUserAssignmentTests(userId);
            req.setAttribute(RequestParameterName.USER_ASSIGNMENT, userAssignedTests);

            session.setAttribute(SessionAttributeName.QUERY_STRING, req.getQueryString());
            forwardToPage(req, resp, JspPageName.ASSIGNED_TESTS_PAGE);

        } catch (TestServiceException e) {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ShowAssignedTestsPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowAssignedTestsPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
