package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GetResult implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int assignmentId = Integer.parseInt(request.getParameter(RequestParameterName.ASSIGNMENT_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestResultService testResultService = serviceFactory.getTestResultService();
        TestService testService = serviceFactory.getTestService();
        HttpSession session = request.getSession();


        try {
            Assignment assignment = testService.getAssignment(assignmentId);
            Test testInfo = testService.getTestInfo(assignment.getTest().getId());
            Result result = testResultService.getResult(assignment);

            double percentageOfCorrectAnswers = testResultService.calculatePercentageOfCorrectAnswers(result, testInfo);

            request.setAttribute("percentage", percentageOfCorrectAnswers);
            request.setAttribute(RequestParameterName.TEST_NAME, testInfo.getTitle());
            session.setAttribute(SessionAttributeName.COMMAND_NAME, request.getQueryString());
            forwardToPage(request, response, JspPageName.TEST_RESULT_PAGE);

        } catch (ForwardCommandException | TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
