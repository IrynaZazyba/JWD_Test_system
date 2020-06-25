package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ShowAdminAssignTest implements Command {
    private static Logger logger = LogManager.getLogger();


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        try {
            BuildRequest.addFilterInfoToRequest(request);
            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());

            forwardToPage(request, response, JspPageName.ADMIN_PAGE_ASSIGN_TEST);
        } catch (ServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ShowAdminAssignTest command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);

        }
    }


     static class BuildRequest {

         static void addFilterInfoToRequest(HttpServletRequest request) throws ServiceException {
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestService testService = serviceFactory.getTestService();
            UserService userService = serviceFactory.getUserService();

            List<Type> typeWithTests = testService.allTestsType();

            request.setAttribute(RequestParameterName.LIST_TYPE_WITH_TESTS, typeWithTests);
            Set<User> students = userService.getStudents();
            request.setAttribute(RequestParameterName.SET_STUDENTS, students);
            request.setAttribute(RequestParameterName.CURRENT_DATE, LocalDate.now());
        }

    }

}
