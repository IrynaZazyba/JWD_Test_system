package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.CommandName;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class ShowTestsPage implements Command {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestService testService = ServiceFactory.getInstance().getTestService();

        HttpSession session = req.getSession();

        try {
            Set<Type> tests = testService.typeWithTests();
            req.setAttribute(RequestParameterName.TESTS_TYPE_LIST, tests);
            session.setAttribute(SessionAttributeName.COMMAND_NAME, CommandName.SHOW_TESTS_PAGE.toString());
            forwardToPage(req, resp, JspPageName.START_MENU_PAGE);

        } catch (ServiceException | ForwardCommandException e) {
            logger.log(Level.ERROR,e.getMessage(),e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
