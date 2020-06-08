package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CompleteTestCreation implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));

        String answer = null;

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.completeTestCreation(testId);
            response.setStatus(204);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        }
        return answer;
    }
}
