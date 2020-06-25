package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.exception.ServiceException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class TestsResults implements Command {

    private static Logger logger = LogManager.getLogger();


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            ShowAdminAssignTest.BuildRequest.addFilterInfoToRequest(request);
            HttpSession session = request.getSession(false);
            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());

            forwardToPage(request, response, JspPageName.ADMIN_PAGE_TESTS_RESULTS);
        } catch (ForwardCommandException ex ){
            logger.log(Level.ERROR,"Forward to page Exception in TestsResults command", ex);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }


    }
}
