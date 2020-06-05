package by.jwd.testsys.controller.command.front.impl.edit;

import by.jwd.testsys.bean.Test;
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

public class ShowEditTestPage implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();
        TestService testService = serviceFactory.getTestService();

        HttpSession session = request.getSession();

        try {

            adminService.changeTestIsEdited(testId, true);

            Test testData = adminService.receiveTestWithQuestionsAndAnswers(testId);
            List<Type> testTypes = testService.allTestsType();
            request.setAttribute("testData", testData);
            request.setAttribute("testsTypes", testTypes);
            session.setAttribute(SessionAttributeName.COMMAND_NAME, request.getQueryString());
            forwardToPage(request, response, JspPageName.EDIT_TEST);

        } catch (ForwardCommandException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (AdminServiceException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }
}
