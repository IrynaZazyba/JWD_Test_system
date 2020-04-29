package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class SaveAnswer implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        TestLogService testService = ServiceFactory.getInstance().getTestLogService();

        int question_log_id = Integer.parseInt(request.getParameter("question_log_id"));
        String[] answersId = request.getParameterValues("answer");
        try {
            testService.writeUserAnswer(question_log_id, answersId);

        } catch (TestLogServiceException e) {
            response.setStatus(500);
            Map<String, Object> map = new HashMap<>();
            map.put("page", "errorPage.jsp");
            Gson gson = new Gson();
            return gson.toJson(map);
        }

        return null;
    }
}
