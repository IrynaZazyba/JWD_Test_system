package by.jwd.testsys.controller;

import by.jwd.testsys.controller.parameters.RequestParameterName;
import by.jwd.testsys.logic.core.Command;
import by.jwd.testsys.logic.core.CommandProvider;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = "/test", name = "CoreController")
public class CoreController extends HttpServlet {

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
        Command command = commandProvider.getCommand(commandName);
        command.execute(req, resp);
    }


    @Override
    public void destroy() {
        super.destroy();
    }

}
