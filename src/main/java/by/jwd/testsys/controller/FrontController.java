package by.jwd.testsys.controller;

import by.jwd.testsys.controller.command.CommandProvider;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.parameter.RequestParameterName;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/test", name = "FrontController")
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = -7674451632663324163L;

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
        this.doProcess(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doProcess(req, resp);
    }


    private void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(RequestParameterName.COMMAND_NAME);
        CommandProvider commandProvider = CommandProvider.getInstance();
        Command command = commandProvider.getFrontCommand(commandName);
        command.execute(req, resp);

    }


    @Override
    public void destroy() {
        super.destroy();
    }

}
