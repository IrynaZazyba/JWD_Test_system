package by.jwd.testsys.controller.command.front.impl.edit;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.command.util.GetParameterFromRequestHelper;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
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
import java.util.Set;

public class ShowAdminPanel implements Command {

    private static Logger logger = LogManager.getLogger(ShowAdminPanel.class);
    private final static int NUMBER_OF_RECORDS_PER_PAGE=11;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        HttpSession session = request.getSession(false);

        try {

            List<Type> types = testService.allTestsType();
            int activeTypeId = GetParameterFromRequestHelper.getActiveTypeId(request, types);
            int page = GetParameterFromRequestHelper.getCurrentPage(request);

            Set<Test> testByTypeId = testService.getTestByTypeId(activeTypeId, page, NUMBER_OF_RECORDS_PER_PAGE);

            request.setAttribute(RequestParameterName.TEST_TYPES_LIST, types);
            request.setAttribute(RequestParameterName.INFO_ABOUT_TESTS, testByTypeId);
            request.setAttribute(RequestParameterName.ACTIVE_TYPE_ID, activeTypeId);
            request.setAttribute(RequestParameterName.CURRENT_PAGE, page);

            int countPages = testService.receiveNumberTestPages(activeTypeId,NUMBER_OF_RECORDS_PER_PAGE,true,true);
            request.setAttribute(RequestParameterName.COUNT_PAGES, countPages);

            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
            forwardToPage(request, response, JspPageName.ADMIN_PANEL);

        } catch (TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ShowAdminPanel command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowAdminPanel command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }
}
