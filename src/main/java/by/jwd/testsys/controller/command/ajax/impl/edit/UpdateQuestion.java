package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateQuestion implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(UpdateQuestion.class);
    private final static String SPLIT_PARAMETER = "-";


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int questionId = 0;
        String question = null;
        Map<Integer, String> answers = new HashMap<>();
        Map<Integer, String> addedAnswers = new HashMap<>();

        List<Integer> rightAnswerId = new ArrayList<>();
        List<Integer> rightAddedAnswerId = new ArrayList<>();

        String deletedAnswers = request.getParameter(RequestParameterName.DELETED_ANSWERS);

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            String key = entry.getKey();

            if (key.contains(RequestParameterName.UPDATED_QUESTION_ID)) {
                question = entry.getValue()[0];
                questionId = Integer.parseInt(key.split(SPLIT_PARAMETER)[1]);
            }

            if (key.contains(RequestParameterName.UPDATED_QUESTION_ANSWER_ID)) {
                answers.put(Integer.parseInt(key.split(SPLIT_PARAMETER)[1]), entry.getValue()[0]);
            }

            if (key.contains(RequestParameterName.UPDATED_QUESTION_RIGHT_ANSWER_ID)) {
                rightAnswerId.add(Integer.parseInt(key.split(SPLIT_PARAMETER)[1]));
            }

            if (key.contains(RequestParameterName.UPDATED_QUESTION_ADDED_ANSWER_ID)) {
                addedAnswers.put(Integer.parseInt(key.split(SPLIT_PARAMETER)[1]), entry.getValue()[0]);
            }

            if (key.contains(RequestParameterName.UPDATED_QUESTION_ADDED_RIGHT_ANSWER_ID)) {
                rightAddedAnswerId.add(Integer.parseInt(key.split(SPLIT_PARAMETER)[1]));
            }
        }

        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.updateQuestionWithAnswers(questionId, question, deletedAnswers, answers, addedAnswers, rightAnswerId, rightAddedAnswerId);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in UpdateTestInfo command method execute()",e);
            response.setStatus(409);
        }

        return answer;
    }


}
