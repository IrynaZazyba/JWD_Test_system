package by.jwd.testsys.logic.logicCommand.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.logicCommand.ForwardCommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.TestService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ShowMainPage implements Command {

    private Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestService testService = ServiceFactory.getInstance().getTestService();

        HttpSession session = req.getSession();

        try {
            Set<Type> tests = testService.getTypeWithTests();
            req.setAttribute(RequestParameterName.TESTS_TYPE_LIST, tests);
            req.setAttribute("tests", tests);
            session.setAttribute("command", "show_main_page");
            forwardToPage(req, resp, JspPageName.START_JSP_PAGE);

        } catch (ServiceException | ForwardCommandException e) {
            logger.log(Level.ERROR, e.getMessage());
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
