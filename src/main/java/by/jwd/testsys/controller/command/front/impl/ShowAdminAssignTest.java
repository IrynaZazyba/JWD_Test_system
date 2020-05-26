package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ShowAdminAssignTest implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceFactory serviceFactory=ServiceFactory.getInstance();
        TestService testService =serviceFactory.getTestService();
        UserService userService = serviceFactory.getUserService();

        try {
            List<Type> typeWithTests = testService.allTestsType();

            request.setAttribute("type_tests",typeWithTests);
            Set<User> students = userService.getStudents();
            request.setAttribute("users", students);
            request.setAttribute("dateNow", LocalDate.now());

            forwardToPage(request, response, JspPageName.ADMIN_PAGE_ASSIGN_TEST);
        } catch (ForwardCommandException | ServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
