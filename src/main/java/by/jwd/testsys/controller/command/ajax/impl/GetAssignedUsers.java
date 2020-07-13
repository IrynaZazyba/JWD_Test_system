package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
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


        int testId = 0;
        String testIdValue = request.getParameter(RequestParameterName.TEST_ID);
        if (testIdValue != null) {
            testId = Integer.parseInt(testIdValue);
        }

        int testTypeId = 0;
        String testTypeIdValue = request.getParameter(RequestParameterName.TEST_TYPE_ID);
        if (testTypeIdValue != null) {
            testTypeId = Integer.parseInt(testTypeIdValue);
        }

        String completed = request.getParameter(RequestParameterName.COMPLETED);
        boolean isCompleted =parseBooleanParameter(completed);

        String answer = null;

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        try {
            Set<User> usersWithAssignment = userService.getUsersWithAssignment(testId, testTypeId, isCompleted);

            Map<String, Set<User>> dataToPage = new HashMap<>();
            Gson gson = new Gson();
            dataToPage.put(RequestParameterName.USERS_INFO_ABOUT_TESTS, usersWithAssignment);
            answer = gson.toJson(dataToPage);

        } catch (UserServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "InvalidUserData Exception in GetAssignedUsers command", e);
            response.setStatus(409);
        }

        return answer;
    }


    //todo в отдельный класс
    private boolean parseBooleanParameter(String booleanParam) {

        if (booleanParam.equalsIgnoreCase("true") ||
                booleanParam.equalsIgnoreCase("on") ||
                booleanParam.equalsIgnoreCase("yes")) {
            return true;
        } else
            if (booleanParam.equalsIgnoreCase("false") ||
                booleanParam.equalsIgnoreCase("off") ||
                booleanParam.equalsIgnoreCase("no")) {
            return false;
        } else {
                throw  new NumberFormatException("Parameter "+booleanParam+" is not a boolean");
        }
    }

}
