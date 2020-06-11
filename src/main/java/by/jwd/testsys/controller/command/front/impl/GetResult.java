package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
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
import java.time.LocalDateTime;

public class GetResult implements Command {
    private static Logger logger = LogManager.getLogger();


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int assignmentId = Integer.parseInt(request.getParameter(RequestParameterName.ASSIGNMENT_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        HttpSession session = request.getSession();


        try {
            Assignment assignment = testService.getAssignment(assignmentId);

            if (!assignment.isComplete()) {
                testService.completeTest(assignment, LocalDateTime.now());
            }

            Test testInfo = testService.getTestInfo(assignment.getTest().getId());
            double percentageOfCorrectAnswers = testService.calculatePercentageOfCorrectAnswers(assignment, testInfo);

            request.setAttribute(RequestParameterName.PERCENTAGE_CORRECT_ANSWERS, percentageOfCorrectAnswers);
            request.setAttribute(RequestParameterName.TEST_NAME, testInfo.getTitle());
            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
            forwardToPage(request, response, JspPageName.TEST_RESULT_PAGE);

        } catch (TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR,"Forward to page Exception in GetResult command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
