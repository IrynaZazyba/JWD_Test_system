package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.exception.UserServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetAssignedUsers implements AjaxCommand {

    private static Logger logger = LogManager.getLogger(GetAssignedUsers.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {


        String testIdValue = request.getParameter(RequestParameterName.TEST_ID);
        int testId=0;
        if (testIdValue != null) {
            testId = Integer.parseInt(testIdValue);
        }

        String testTypeIdValue = request.getParameter(RequestParameterName.TEST_TYPE_ID);
        int testTypeId=0;
        if (testTypeIdValue != null) {
            testTypeId = Integer.parseInt(testTypeIdValue);
        }



        String completed = request.getParameter("completed");

        String answer = null;
        Map<String, Set<User>> usersWithTestAssignment = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        boolean isCompleted = false;
        if (completed != null) {
            isCompleted = true;
        }

        try {
            Set<User> usersWithAssignment = userService.getUsersWithAssignment(testId, testTypeId, isCompleted);
            usersWithTestAssignment.put("setUsers", usersWithAssignment);
            answer = gson.toJson(usersWithTestAssignment);

        } catch (UserServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in GetAssignedUsers command", e);
            response.setStatus(500);
        }

        return answer;
    }


}
