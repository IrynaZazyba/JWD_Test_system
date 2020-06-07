package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShowQuestion implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        TestLogService testLogService = serviceFactory.getTestLogService();

        int test_id = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        String key = request.getParameter("key");

        HttpSession session = request.getSession(false);
        int user_id = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        Assignment assignment = null;
        try {

            testService.checkPermission(user_id, test_id, key);
            assignment = testService.receiveTestAssignment(test_id, user_id);


            int questionLogId;
            Question questionByTestId = testService.getQuestionByTestId(assignment);


            if (questionByTestId != null) {
                questionLogId = testLogService.writeQuestionLog(questionByTestId.getId(), assignment.getId());


                long time= testService.calculateTestDuration(assignment);

                System.out.println("time "+time);

                Map<String, Object> map = new HashMap<>();
                map.put("question", questionByTestId);
                map.put("assignId", assignment.getId());
                map.put("question_log_id", questionLogId);
                map.put("duration", time);
                Gson gson = new Gson();
                answer = gson.toJson(map);
            } else {

                LocalDateTime testEndTime = LocalDateTime.now();
                testService.completeTest(assignment, testEndTime);

                Map<String, Object> map = new HashMap<>();
                map.put("assignId", assignment.getId());
                Gson gson = new Gson();
                answer = gson.toJson(map);
            }

        } catch (TestLogServiceException | TestServiceException | InvalidUserDataException e) {
            response.setStatus(500);
            Map<String, Object> map = new HashMap<>();
            map.put("page", "errorPage.jsp");
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (TimeIsOverServiceException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("time_is_over", "true");
            map.put("assignId", assignment.getId());
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (InvalidTestKeyException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("invalid_key", "true");
            Gson gson = new Gson();
            return gson.toJson(map);
        }
        return answer;
    }
}
