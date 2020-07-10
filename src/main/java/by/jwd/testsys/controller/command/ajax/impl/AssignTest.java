package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class AssignTest implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(AssignTest.class);
    private final static String EMAIL_FAILURE = "emailFailure";
    private final static String SUCCESS_ASSIGNMENT = "successAssignment";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String answer = "";
        Map<String, Set<User>> assignmentResult;
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        Gson gson = gsonBuilder.create();

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        String deadlineParam = request.getParameter(RequestParameterName.DEADLINE_DATE);
        String[] usersId = request.getParameterValues(RequestParameterName.ASSIGNED_USERS);

        if (testId == 0 || deadlineParam == null || usersId == null) {
            response.setStatus(400);
        }

        if (testId != 0 && deadlineParam != null && usersId != null) {

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            AdminService adminService = serviceFactory.getAdminService();

            try {
                LocalDate deadline = LocalDate.parse(deadlineParam);
                assignmentResult = adminService.assignTestToUsers(testId, deadline, usersId);
                boolean successAssignment = adminService.
                        sendTestKeyToUsers(assignmentResult.get(SUCCESS_ASSIGNMENT), testId, deadline);

                JsonElement jsonAnswer = gson.toJsonTree(assignmentResult);

                if (!successAssignment) {
                    jsonAnswer.getAsJsonObject().addProperty(EMAIL_FAILURE, EMAIL_FAILURE);
                }

                answer = gson.toJson(jsonAnswer);

            } catch (AdminServiceException e) {
                response.setStatus(500);
            } catch (InvalidUserDataException e) {
                logger.log(Level.ERROR, "Invalid user data in AssignTest command method execute()", e);
                response.setStatus(409);
            }
        }
        return answer;
    }

}
