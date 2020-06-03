package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class AddTestPage implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        HttpSession session = request.getSession();


        try {

            List<Type> types = testService.allTestsType();

            session.setAttribute(SessionAttributeName.COMMAND_NAME, request.getQueryString());
            request.setAttribute("testTypes",types);
            forwardToPage(request, response, JspPageName.ADD_TEST);

        } catch (ForwardCommandException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
