package by.jwd.testsys.controller.command.front.impl.edit;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ShowAdminPanel implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        HttpSession session = request.getSession(false);

        try {

            List<Type> types = testService.allTestsType();
            String typeId = request.getParameter(RequestParameterName.TEST_TYPE_ID);
            int activeTypeId = 0;
            if (typeId != null) {
                activeTypeId = Integer.parseInt(typeId);
            } else {
                activeTypeId = types.get(0).getId();
            }

            Set<Test> testByTypeId = testService.getAllTestByTypeId(activeTypeId);

            request.setAttribute(RequestParameterName.TEST_TYPES_LIST, types);
            request.setAttribute(RequestParameterName.INFO_ABOUT_TESTS, testByTypeId);
            request.setAttribute(RequestParameterName.ACTIVE_TYPE_ID, activeTypeId);

            session.setAttribute(SessionAttributeName.QUERY_STRING, request.getQueryString());
            request.getRequestDispatcher(JspPageName.ADMIN_PANEL).forward(request, response);

        } catch (ServiceException e) {
            response.sendRedirect(JspPageName.ERROR_PAGE);
        }


    }
}
