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

public class CreateQuestionAnswer implements AjaxCommand {

    private static Logger logger = LogManager.getLogger(CreateQuestionAnswer.class);
    private final static String EMPTY_STRING = "";
    private final static String SPLIT_PARAMETER = "-";
    private final static String ANSWER_PAIR_ID_VALUE="answer";
    private final static String RIGHT_ANSWER_ID_VALUE="check";


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        Map<Integer, String> answers = new HashMap<>();
        List<Integer> rightAnswerId = new ArrayList<>();

        String question = request.getParameter(RequestParameterName.QUESTION_TITLE);
        request.getParameterMap().forEach((k, v) -> {
            if (k.contains(ANSWER_PAIR_ID_VALUE) && !v[0].equals(EMPTY_STRING)) {
                int id = Integer.parseInt(k.split(SPLIT_PARAMETER)[1]);
                answers.put(id, v[0]);
            }

            if (k.contains(RIGHT_ANSWER_ID_VALUE)) {
                int id = Integer.parseInt(k.split(SPLIT_PARAMETER)[1]);
                rightAnswerId.add(id);
            }
        });
        String answer = null;

        if (answers.size() > 4) {
            response.setStatus(409);
        } else {

            int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));

            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            AdminService adminService = serviceFactory.getAdminService();

            try {
                adminService.createQuestionAnswer(question, answers, rightAnswerId, testId);
                response.setStatus(204);
            } catch (AdminServiceException e) {
                response.setStatus(500);
            } catch (InvalidUserDataException e) {
                logger.log(Level.ERROR, "Invalid user data in CreateQuestionAnswer command method execute()", e);
                response.setStatus(409);
            }
        }
        return answer;
    }


}
