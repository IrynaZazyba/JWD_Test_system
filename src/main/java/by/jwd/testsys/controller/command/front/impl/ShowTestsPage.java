package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.GetParameterFromRequestHelper;
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

public class ShowTestsPage implements Command {

    private static Logger logger = LogManager.getLogger(ShowTestsPage.class);
    private final static int NUMBER_OF_RECORDS_PER_PAGE = 8;


    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestService testService = serviceFactory.getTestService();

            List<Type> types = testService.allTestsType();

            int activeTypeId = GetParameterFromRequestHelper.getActiveTypeId(req, types);
            int page = GetParameterFromRequestHelper.getCurrentPage(req);

            HttpSession session = req.getSession();
            int userId = (Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

            Set<Test> testByTypeId = testService.getTestsPermittedForUser(activeTypeId,userId, page, NUMBER_OF_RECORDS_PER_PAGE);

            req.setAttribute(RequestParameterName.TEST_TYPES_LIST, types);
            req.setAttribute(RequestParameterName.INFO_ABOUT_TESTS, testByTypeId);
            req.setAttribute(RequestParameterName.ACTIVE_TYPE_ID, activeTypeId);
            req.setAttribute(RequestParameterName.CURRENT_PAGE, page);

            String testTypeTitle=null;
            for (Type type : types) {
                if (type.getId() == activeTypeId) {
                    testTypeTitle = type.getTitle();
                }
            }

            req.setAttribute(RequestParameterName.TEST_TYPE_TITLE, testTypeTitle);
            int countPages = testService.receiveNumberTestPages(activeTypeId, NUMBER_OF_RECORDS_PER_PAGE, false, false);
            req.setAttribute(RequestParameterName.COUNT_PAGES, countPages);

            session.setAttribute(SessionAttributeName.QUERY_STRING, req.getQueryString());
            forwardToPage(req, resp, JspPageName.TEST_MENU);

        } catch (TestServiceException e) {
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ShowTestsPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowTestsPage command", e);
            resp.sendRedirect(JspPageName.ERROR_PAGE);
        }
    }


}
