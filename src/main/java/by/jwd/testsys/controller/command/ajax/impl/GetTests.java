package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Test;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetTests implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int typeId=Integer.parseInt(request.getParameter("typeId"));

        String answer=null;
        Map<String, Object> responseParams = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        try {
            Set<Test> tests = testService.getTestByTypeId(typeId);
            responseParams.put("tests", tests);
            answer=gson.toJson(responseParams);

        } catch (ServiceException e) {
            //todo
            e.printStackTrace();
        }


        return answer;
    }


}
