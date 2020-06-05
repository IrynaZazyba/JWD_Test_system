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

public class CreateTest implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String testId = request.getParameter(RequestParameterName.TEST_ID);
        System.out.println("testId "+testId);
        int typeId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_TYPE_ID));
        String testTitle = request.getParameter(RequestParameterName.TEST_TITLE);
        String testKey = request.getParameter(RequestParameterName.TEST_KEY);
        String duration = request.getParameter(RequestParameterName.TEST_DURATION);
        int testDuration = 0;
        if (duration != null) {
            testDuration = Integer.parseInt(duration);
        }
        String answer = null;
        Map<String, Object> parameterMapForJson = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {

            if (testId != null) {
                adminService.updateTestData(Integer.parseInt(testId), typeId, testTitle, testKey, testDuration);
            }

            if (testId == null) {
                int createdTestId = adminService.createTest(typeId, testTitle, testKey, testDuration);
                parameterMapForJson.put("testId", createdTestId);
                answer = gson.toJson(parameterMapForJson);
            }
        } catch (AdminServiceException e) {
            response.setStatus(500);
        }
        return answer;
    }
}
