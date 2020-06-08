package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Result;
import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ShowExeTestPage implements Command {


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int testId = Integer.parseInt(req.getParameter(RequestParameterName.TEST_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        HttpSession session = req.getSession(false);
        int user_id = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        Test test = null;
        try {
            Result result = testService.checkResult(user_id, testId);
            test = testService.getTestInfo(testId);
            test.setStarted(result!=null);
            req.setAttribute(RequestParameterName.TEST_INFO, test);
            session.setAttribute(SessionAttributeName.QUERY_STRING, req.getQueryString());

            forwardToPage(req, resp, JspPageName.EXE_TEST_PAGE);
        } catch (TestServiceException | ForwardCommandException e) {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }


    }
}
