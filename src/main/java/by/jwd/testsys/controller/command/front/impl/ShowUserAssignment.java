package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ShowUserAssignment implements Command {

//todo удалить
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        HttpSession session = request.getSession();
//        int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
//
//        ServiceFactory serviceFactory=ServiceFactory.getInstance();
//        TestService testService = serviceFactory.getTestService();
//
//        try {
//            Set<Test> userAssignedTests=testService.getUserAssignmentTests(userId);
//            request.setAttribute("userAssignedTests",userAssignedTests);
//            session.setAttribute(SessionAttributeName.QUERY_STRING,request.getQueryString());
//
//        } catch (ServiceException e) {
//            response.sendRedirect(JspPageName.ERROR_PAGE);
//        }


    }
}
