package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.ExistsTypeAdminServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class AddTestType implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String testTypeTitle = request.getParameter(RequestParameterName.TEST_TYPE_TITLE);

        Map<String, Object> parameterMapForJson = new HashMap<>();
        Gson gson = new Gson();
        String answer = null;

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.addTestType(testTypeTitle);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (ExistsTypeAdminServiceException e) {
            response.setStatus(409);
        }
        return answer;
    }
}
