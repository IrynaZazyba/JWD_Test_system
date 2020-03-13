package by.jwd.testsys.controller;

import by.jwd.testsys.logic.Command;
import by.jwd.testsys.logic.CommandProvider;
import by.jwd.testsys.logic.exception.CommandException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {


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
        String commandName = req.getParameter(RequestParametrName.COMMAND_NAME);
        Command command = CommandProvider.getInstance().getCommand(commandName.toUpperCase());

        String page = null;
        try {
            page = command.execute(req);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(page);
        if (requestDispatcher != null) {
            requestDispatcher.forward(req, resp);
        } else {
            errorMessage(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

    }

    @Override
    public void destroy() {

        super.destroy();
    }

    private void errorMessage(HttpServletResponse response) {
        response.setContentType("text/html");
        try {
            response.getWriter().println("E R R O R");
        } catch (IOException e) {
            System.out.println("print E R R O R");
            e.printStackTrace();
        }

    }
}
