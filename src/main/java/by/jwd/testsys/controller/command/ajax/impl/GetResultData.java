package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Result;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetResultData implements AjaxCommand {

    private static Logger logger = LogManager.getLogger(GetResultData.class);
    private static final String EMPTY_STRING = "";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        LocalDate deadline = null;
        int userId = 0;
        int testTypeId = 0;
        int testId = 0;

        String typeIdParam = request.getParameter(RequestParameterName.TEST_TYPE_ID);
        if (!typeIdParam.equals(EMPTY_STRING)) {
            testTypeId = Integer.parseInt(typeIdParam);
        }
        String testIdParam = request.getParameter(RequestParameterName.TEST_ID);
        if (!testIdParam.equals(EMPTY_STRING)) {
            testId = Integer.parseInt(testIdParam);
        }
        String assignedUserId = request.getParameter(RequestParameterName.USER_ID);
        if (!assignedUserId.equals(EMPTY_STRING)) {
            userId = Integer.parseInt(assignedUserId);
        }

        String deadlineDate = request.getParameter(RequestParameterName.DATE);
        if (!deadlineDate.equals(EMPTY_STRING)) {
            deadline = LocalDate.parse(deadlineDate);
        }

        String answer = null;
        Map<String, Set<Result>> assignmentResult = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        try {
            Set<Result> results = testService.receiveResultData(testTypeId, testId, userId, deadline);
            assignmentResult.put(RequestParameterName.TEST_RESULTS, results);
            answer = gson.toJson(assignmentResult);
        } catch (TestLogServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            response.setStatus(409);
            logger.log(Level.ERROR, "InvalidUserData Exception in ShowResultData command method execute()", e);
        }
        return answer;
    }


}
