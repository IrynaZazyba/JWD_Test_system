package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Statistic;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestResultService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class DisplayStatistic implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        try {

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestResultService testResultService = serviceFactory.getTestResultService();
            Set<Statistic> userTestStatistic = testResultService.getUserTestStatistic(userId);
            request.setAttribute("statisticSet", userTestStatistic);

            forwardToPage(request, response, JspPageName.STATISTIC_PAGE);
        } catch (ForwardCommandException | TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);

        }


    }
}
