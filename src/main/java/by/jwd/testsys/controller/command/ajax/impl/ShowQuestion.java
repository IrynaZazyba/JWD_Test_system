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
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
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


        int test_id = Integer.parseInt(request.getParameter("test_id"));
        String key = request.getParameter("key");

        HttpSession session = request.getSession(false);
        int user_id = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);


        try {

            Assignment assignment = testService.exeTest(test_id, user_id, key);

            int questionLogId;

            Question questionByTestId = testService.getQuestionByTestId(assignment);


            if (questionByTestId != null) {
                questionLogId = testLogService.writeQuestionLog(questionByTestId.getId(), assignment.getId());

                LocalDateTime startTestTime = testService.getStartTestTime(assignment.getId());
                long time = Timestamp.valueOf(startTestTime).getTime();

                Map<String, Object> map = new HashMap<>();
                map.put("question", questionByTestId);
                map.put("status", "ok");
                map.put("question_log_id", questionLogId);
                if(key!=null){
                map.put("time_start", 30);}
                Gson gson = new Gson();
                answer = gson.toJson(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("assign_id", assignment.getId());
                map.put("status", "ok");
                Gson gson = new Gson();
                answer = gson.toJson(map);
            }

        } catch (TestLogServiceException | ImpossibleTestDataServiceException | TestServiceException e) {
            response.setStatus(500);
            Map<String, Object> map = new HashMap<>();
            map.put("page", "errorPage.jsp");
            Gson gson = new Gson();
            return gson.toJson(map);
        }
        return answer;
    }
}
