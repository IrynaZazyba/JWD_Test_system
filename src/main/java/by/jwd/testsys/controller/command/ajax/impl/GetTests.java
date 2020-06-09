package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetTests implements AjaxCommand {

    private final static String RESPONSE_PARAMETER_SET_TESTS="tests";


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int typeId=Integer.parseInt(request.getParameter(RequestParameterName.TEST_TYPE_ID));

        String answer=null;
        Map<String, Object> responseParams = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        try {
            Set<Test> tests = testService.getNotEditedTestByTypeId(typeId);
            responseParams.put(RESPONSE_PARAMETER_SET_TESTS, tests);
            answer=gson.toJson(responseParams);

        } catch (ServiceException e) {
           response.setStatus(500);
        }

        return answer;
    }


}
