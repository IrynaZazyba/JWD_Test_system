package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetResult implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int assignmentId = Integer.parseInt(request.getParameter("assign_id"));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestResultService testResultService = serviceFactory.getTestResultService();
        TestService testService = serviceFactory.getTestService();


        try {
            Assignment assignment = testService.getAssignment(assignmentId);
            Test testInfo = testService.getTestInfo(assignment.getTest().getId());
            Result result = testResultService.getResult(assignment);
            double v = testResultService.calculatePercentageOfCorrectAnswers(result,testInfo);
            request.setAttribute("percentage", v);
            request.setAttribute("test_name", testInfo.getTitle());

            forwardToPage(request, response, JspPageName.TEST_RESULT_PAGE);
        } catch (ForwardCommandException e) {
            e.printStackTrace();
        } catch (TestServiceException e) {
            e.printStackTrace();
        }

    }
}
