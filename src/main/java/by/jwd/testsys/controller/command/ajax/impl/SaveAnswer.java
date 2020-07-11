package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.TestLogServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class SaveAnswer implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(SaveAnswer.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        TestLogService testService = ServiceFactory.getInstance().getTestLogService();

        int question_log_id = Integer.parseInt(request.getParameter(RequestParameterName.QUESTION_LOG_ID));
        String[] answersId = request.getParameterValues(RequestParameterName.ANSWER_ID);
        Map<String, Object> dataToPage = new HashMap<>();
        Gson gson = new Gson();

        try {
            if (answersId != null) {
                testService.writeUserAnswer(question_log_id, answersId);
            }

        } catch (TestLogServiceException e) {
            response.setStatus(500);
            dataToPage.put(RequestParameterName.PAGE, JspPageName.ERROR_PAGE);
            return gson.toJson(dataToPage);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in SaveAnswer command method execute()",e);
            response.setStatus(409);
            dataToPage.put(RequestParameterName.PAGE, JspPageName.ERROR_PAGE);
            return gson.toJson(dataToPage);
        }

        return null;
    }
}
