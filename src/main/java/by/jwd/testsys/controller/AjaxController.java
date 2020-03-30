package by.jwd.testsys.controller;

import by.jwd.testsys.controller.parameters.RequestParameterName;
import by.jwd.testsys.logic.ajax.AjaxCommand;
import by.jwd.testsys.logic.ajax.AjaxCommandProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/ajax", name = "AjaxController")
@MultipartConfig
public class AjaxController extends HttpServlet {

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
        this.doProcess(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doProcess(req, resp);
    }


    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ajaxCommandName = request.getParameter(RequestParameterName.COMMAND_NAME);
        AjaxCommandProvider ajaxCommandProvider = AjaxCommandProvider.getInstance();
        AjaxCommand ajaxCommand = ajaxCommandProvider.getAjaxCommand(ajaxCommandName.toUpperCase());

        String jsonAnswer = ajaxCommand.execute(request, response);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonAnswer);
        out.flush();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
