package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Result;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShowResultData implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        LocalDate date = null;
        int userId = 0;
        int testTypeId = 0;
        int testId = 0;

        if (!request.getParameter("testTypeId").equals("")) {
            testTypeId = Integer.parseInt(request.getParameter("testTypeId"));
        }
        if (!request.getParameter(RequestParameterName.TEST_ID).equals("")) {
            testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        }
        if (!request.getParameter("assigned_users").equals("")) {
            userId = Integer.parseInt(request.getParameter("assigned_users") );
        }

        if (!request.getParameter(RequestParameterName.DATE).equals("")) {
            date = LocalDate.parse(request.getParameter(RequestParameterName.DATE));
        }


        String answer = null;
        Map<String, Set<Result>> assignmentResult = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestLogService testLogService = serviceFactory.getTestLogService();
        try {
            Set<Result> results = testLogService.receiveResultData(testTypeId, testId, userId, date);
            assignmentResult.put("results", results);
            answer=gson.toJson(assignmentResult);

        } catch (TestLogServiceException e) {
            e.printStackTrace();
        }


        return answer;
    }


}
