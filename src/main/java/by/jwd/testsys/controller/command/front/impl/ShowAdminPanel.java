package by.jwd.testsys.controller.command.front.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.Type;
import by.jwd.testsys.controller.command.front.Command;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ShowAdminPanel implements Command {


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        try {

            List<Type> types = testService.allTestsType();
            request.setAttribute("types", types);
            String typeId = request.getParameter("typeId");
            int activeId = 0;
            if (typeId != null) {
                activeId = Integer.parseInt(typeId);
            } else {
                activeId = types.get(0).getId();
            }

            Set<Test> testByTypeId = testService.getTestByTypeId(activeId);

            request.setAttribute("types", types);
            request.setAttribute("tests", testByTypeId);
            request.setAttribute("activeId", activeId);

        } catch (ServiceException e) {
            e.printStackTrace();
        }


        request.getRequestDispatcher(JspPageName.ADMIN_PANEL).forward(request, response);

    }
}
