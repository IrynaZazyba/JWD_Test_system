package by.jwd.testsys.controller;

import by.jwd.testsys.dao.dbconn.ConnectionPool;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.logicCommand.CommandProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/test")
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
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String commandName = req.getParameter(RequestParameterName.COMMAND_NAME);

        CommandProvider commandProvider = CommandProvider.getInstance();
        Command command = commandProvider.getCommand(commandName.toUpperCase());

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
