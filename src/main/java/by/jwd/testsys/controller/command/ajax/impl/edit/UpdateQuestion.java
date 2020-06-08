package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateQuestion implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int questionId = 0;
        String question = null;
        Map<Integer, String> answers = new HashMap<>();
        Map<Integer, String> addedAnswers = new HashMap<>();

        List<Integer> rightAnswerId = new ArrayList<>();
        List<Integer> rightAddedAnswerId = new ArrayList<>();

        String deletedAnswers = request.getParameter("deletedAnswers");

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            String key = entry.getKey();

            if (key.contains("question-")) {
                question = entry.getValue()[0];
                questionId = Integer.parseInt(key.split("-")[1]);
            }

            if (key.contains("answerAdd")) {
                addedAnswers.put(Integer.parseInt(key.split("-")[1]), entry.getValue()[0]);
            }

            if (key.contains("answer-")) {
                answers.put(Integer.parseInt(key.split("-")[1]), entry.getValue()[0]);
            }

            if (key.contains("check-")) {
                rightAnswerId.add(Integer.parseInt(key.split("-")[1]));
            }

            if (key.contains("checkAdd")) {
                rightAddedAnswerId.add(Integer.parseInt(key.split("-")[1]));
            }
        }

        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.updateQuestionWithAnswers(questionId, question, deletedAnswers, answers, addedAnswers, rightAnswerId, rightAddedAnswerId);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        }

        return answer;
    }


}
