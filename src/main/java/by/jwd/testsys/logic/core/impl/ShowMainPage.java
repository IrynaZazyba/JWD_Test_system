package by.jwd.testsys.logic.core.impl;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.parameters.JspPageName;
import by.jwd.testsys.controller.parameters.RequestParameterName;
import by.jwd.testsys.controller.parameters.SessionAttributeName;
import by.jwd.testsys.logic.core.Command;
import by.jwd.testsys.logic.core.CommandName;
import by.jwd.testsys.logic.core.ForwardCommandException;
import by.jwd.testsys.logic.service.exception.ServiceException;
import by.jwd.testsys.logic.service.TestService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            session.setAttribute(SessionAttributeName.COMMAND_NAME, CommandName.SHOW_MAIN_PAGE);
            forwardToPage(req, resp, JspPageName.START_MENU_PAGE);

        } catch (ServiceException | ForwardCommandException e) {
            logger.log(Level.ERROR, e.getMessage());
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }
}
