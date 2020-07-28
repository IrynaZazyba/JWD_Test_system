package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetTests implements AjaxCommand {



    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int typeId=Integer.parseInt(request.getParameter(RequestParameterName.TEST_TYPE_ID));

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        String answer=null;

        try {
            Set<Test> tests = testService.getNotEditedTestByTypeId(typeId);

            Map<String, Object> dataToPage = new HashMap<>();
            Gson gson = new Gson();
            dataToPage.put(RequestParameterName.INFO_ABOUT_TESTS, tests);
            answer=gson.toJson(dataToPage);

        } catch (ServiceException e) {
           response.setStatus(500);
        }

        return answer;
    }


}
