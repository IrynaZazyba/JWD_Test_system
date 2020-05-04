package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ImpossibleTestDataServiceException;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.exception.TestServiceException;
import by.jwd.testsys.logic.exception.TimeIsOverServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class ShowQuestion implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        TestLogService testLogService = serviceFactory.getTestLogService();


        int test_id = Integer.parseInt(request.getParameter("test_id"));
        String key = request.getParameter("key");

        HttpSession session = request.getSession(false);
        int user_id = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        Assignment assignment=null;
        try {

            assignment = testService.exeTest(test_id, user_id, key);
            int questionLogId;
            Question questionByTestId = testService.getQuestionByTestId(assignment);


            if (questionByTestId != null) {
                questionLogId = testLogService.writeQuestionLog(questionByTestId.getId(), assignment.getId());

                LocalTime testTime = testService.getTestDuration(assignment.getId());

                long time = testTime.toSecondOfDay();

                Map<String, Object> map = new HashMap<>();
                map.put("question", questionByTestId);
                map.put("assign_id", assignment.getId());
                map.put("question_log_id", questionLogId);
                if (key != null) {
                    map.put("duration", time);
                }
                Gson gson = new Gson();
                answer = gson.toJson(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("assign_id", assignment.getId());
                Gson gson = new Gson();
                answer = gson.toJson(map);
            }

        } catch (TestLogServiceException | ImpossibleTestDataServiceException | TestServiceException e) {
            response.setStatus(500);
            Map<String, Object> map = new HashMap<>();
            map.put("page", "errorPage.jsp");
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (TimeIsOverServiceException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("time_is_over","true");
            map.put("assign_id", assignment.getId());
            Gson gson = new Gson();
            return gson.toJson(map);
        }
        return answer;
    }
}
