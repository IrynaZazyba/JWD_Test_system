package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
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

        int testId = Integer.parseInt(request.getParameter("testId"));
        LocalDate deadline = LocalDate.parse(request.getParameter("date"));
        String[] usersId = request.getParameterValues("users");

        String answer = null;
        Map<String, Object> responseParams = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        try {
            Set<User> users = testService.assignTestToUsers(testId, deadline, usersId);
            if (users.size() != 0) {
                responseParams.put("users", users);
            }
            answer = gson.toJson(responseParams);

        } catch (ServiceException e) {
            //todo
            e.printStackTrace();
        }
        return answer;
    }


}
