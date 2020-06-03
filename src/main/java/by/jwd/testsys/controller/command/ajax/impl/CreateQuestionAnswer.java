package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class CreateQuestionAnswer implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Map<Integer, String> answers = new HashMap<>();
        List<Integer> rightAnswerId = new ArrayList<>();

        String question = request.getParameter("question");
        request.getParameterMap().forEach((k, v) -> {
            if (k.contains("answer")&&!v[0].equals("")) {
                int id = Integer.parseInt(k.split("-")[1]);
                answers.put(id, v[0]);
            }

            if (k.contains("check")) {
                int id = Integer.parseInt(k.split("-")[1]);
                rightAnswerId.add(id);
            }
        });

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));


        String answer = null;

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.createQuestionAnswer(question, answers, rightAnswerId, testId);
            response.setStatus(204);
        } catch (ServiceException e) {
            response.setStatus(500);
        }

        return answer;
    }


}
