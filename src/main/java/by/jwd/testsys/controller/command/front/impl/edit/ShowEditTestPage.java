package by.jwd.testsys.controller.command.front.impl.edit;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.command.front.ForwardCommandException;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.AdminServiceException;
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

public class ShowEditTestPage implements Command {

    private static Logger logger = LogManager.getLogger(ShowEditTestPage.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        try {

            TestEditRequestBuilder.prepareTestInfo(request, testId);

            HttpSession session = request.getSession();
            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
            forwardToPage(request, response, JspPageName.EDIT_TEST);

        } catch (AdminServiceException | TestServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (ForwardCommandException e) {
            logger.log(Level.ERROR, "Forward to page Exception in ShowEditTestPage command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowEditTestPage command", e);
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }

    }


    static class TestEditRequestBuilder {

        static void prepareTestInfo(HttpServletRequest request, int testId) throws AdminServiceException, TestServiceException, InvalidUserDataException {
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            AdminService adminService = serviceFactory.getAdminService();
            TestService testService = serviceFactory.getTestService();

            adminService.changeTestIsEdited(testId, true);
            Test testData = adminService.receiveTestWithQuestionsAndAnswers(testId);
            List<Type> testTypes = testService.allTestsType();

            request.setAttribute(RequestParameterName.FULL_TEST_DATA, testData);
            request.setAttribute(RequestParameterName.TEST_TYPES_LIST, testTypes);

        }

    }


}
