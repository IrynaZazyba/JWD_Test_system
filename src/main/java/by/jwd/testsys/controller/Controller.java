package by.jwd.testsys.controller;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.logic.command.Command;
import by.jwd.testsys.logic.command.CommandProvider;
import by.jwd.testsys.logic.command.CommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.TestService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class Controller extends HttpServlet {

    private static final long serialVersionUID = -7674451632663324163L;

    private static Logger logger = LogManager.getLogger();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(JspPageName.START_MENU_PAGE);
        HttpSession session = req.getSession(false);
        if (requestDispatcher != null && session != null) {
            if (session.getAttribute(SessionAttrinbuteName.USER_LOGIN_SESSION_ATTRIBUTE) == null) {
                resp.sendRedirect("/test-system");
            } else {

                TestService testService = ServiceFactory.getInstance().getTestService();
                List<Type> testsType = null;
                try {
                    testsType = testService.getAllTestsType();
                    req.setAttribute(RequestParameterName.TESTS_TYPE_LIST, testsType);
                } catch (ServiceException e) {
                    resp.sendRedirect(JspPageName.ERROR_PAGE);
                }

                requestDispatcher.forward(req, resp);
            }
        } else {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(RequestParameterName.COMMAND_NAME);
        Command command = CommandProvider.getInstance().getCommand(commandName.toUpperCase());
        try {
            command.execute(req, resp);
        } catch (CommandException e) {
            logger.log(Level.ERROR, "Exception in doPost method");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(JspPageName.ERROR_PAGE);
            if (requestDispatcher != null) {
                requestDispatcher.forward(req, resp);
            }
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().dispose();
        super.destroy();
    }

}
