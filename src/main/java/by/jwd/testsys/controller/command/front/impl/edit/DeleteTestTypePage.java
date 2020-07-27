package by.jwd.testsys.controller.command.front.impl.edit;

import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class DeleteTestTypePage implements Command {

    private static Logger logger = LogManager.getLogger(DeleteTestTypePage.class);


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        HttpSession session = request.getSession();


        try {

            List<Type> types = testService.allTestsType();
            request.setAttribute(RequestParameterName.TEST_TYPES_LIST, types);

            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
            forwardToPage(request, response, JspPageName.DELETE_TEST_TYPE);

        } catch (TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in DeleteTestTypePage command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
