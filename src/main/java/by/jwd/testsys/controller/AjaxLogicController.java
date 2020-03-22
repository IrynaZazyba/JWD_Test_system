package by.jwd.testsys.controller;

import by.jwd.testsys.logic.ajaxCommand.AjaxCommand;
import by.jwd.testsys.logic.ajaxCommand.AjaxCommandProvider;
import by.jwd.testsys.logic.logicCommand.Command;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.logicCommand.CommandProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;


@MultipartConfig
public class AjaxLogicController extends HttpServlet {

    private static final long serialVersionUID = 1071754507052524007L;
    private static Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get");
        this.doPost(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("here");
        String command = req.getParameter("command");
        String ajaxCommandName = req.getParameter(RequestParameterName.COMMAND_NAME);

        AjaxCommand ajaxCommand = AjaxCommandProvider.getInstance().getAjaxCommand(ajaxCommandName.toUpperCase());
        try {
            String jsonAnswer = ajaxCommand.execute(req, resp);
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(jsonAnswer);
            out.flush();
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
        super.destroy();
    }
}
