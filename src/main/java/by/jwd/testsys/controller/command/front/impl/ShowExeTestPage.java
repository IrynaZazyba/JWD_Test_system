package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowExeTestPage implements Command {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int testId = Integer.parseInt(req.getParameter("test_id"));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        //todo
        Test test = null;
        try {
            test = testService.getTestInfo(testId);
        } catch (TestServiceException e) {
            e.printStackTrace();
        }

        req.setAttribute("count_question", test.getCountQuestion());
        req.setAttribute("duration", test.getDuration());
        req.setAttribute("title", test.getTitle());
        req.setAttribute("test_id", test.getId());

        if (test.getKey() != null) {
            req.setAttribute("key", "exist");
        }


        try {
            forwardToPage(req, resp, "WEB-INF/jsp/exe_test.jsp");
        } catch (ForwardCommandException e) {
            //todo
            e.printStackTrace();
        }

    }
}
