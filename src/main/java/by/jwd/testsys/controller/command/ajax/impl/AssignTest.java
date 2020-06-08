package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.DateOutOfRangeException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AssignTest implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String answer = null;
        Map<String, Set<User>> assignmentResult;
        Gson gson = new Gson();

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        String deadlineParam = request.getParameter(RequestParameterName.DEADLINE_DATE);
        String[] usersId = request.getParameterValues(RequestParameterName.ASSIGNED_USERS);

        if (testId==0 || deadlineParam ==null || usersId == null) {
            response.setStatus(400);
        }

        if (testId != 0 && deadlineParam != null && usersId != null) {

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            TestService testService = serviceFactory.getTestService();

            try {
                LocalDate deadline=LocalDate.parse(deadlineParam);
                assignmentResult = testService.assignTestToUsers(testId, deadline, usersId);
                answer = gson.toJson(assignmentResult);

            } catch (ServiceException e) {
                response.setStatus(500);
            } catch (DateOutOfRangeException e) {
                response.setStatus(409);
            }
        }
        return answer;
    }
}
