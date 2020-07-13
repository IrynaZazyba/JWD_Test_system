package by.jwd.testsys.controller.command.ajax.impl;

import by.jwd.testsys.bean.Assignment;
import by.jwd.testsys.bean.Question;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.TestLogService;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.*;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShowQuestion implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(ShowQuestion.class);
    private final static boolean TRUE = true;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String answer;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();
        TestLogService testLogService = serviceFactory.getTestLogService();

        int test_id = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        String key = request.getParameter(RequestParameterName.TEST_KEY);

        HttpSession session = request.getSession(false);
        int user_id = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

        Assignment assignment = null;
        Map<String, Object> dataToPage = new HashMap<>();
        Gson gson = new Gson();
        try {

            assignment = testService.checkTestAssignment(test_id, user_id);
            testService.checkPermission(user_id, test_id, key);

            int questionLogId;
            Question questionByTestId = testService.getQuestionByTestId(assignment);

            if (questionByTestId != null) {
                questionLogId = testLogService.writeQuestionLog(questionByTestId.getId(), assignment.getId());
                long time = testService.calculateTestDuration(assignment);

                dataToPage.put(RequestParameterName.QUESTION_TITLE, questionByTestId);
                dataToPage.put(RequestParameterName.ASSIGNMENT_ID, assignment.getId());
                dataToPage.put(RequestParameterName.QUESTION_LOG_ID, questionLogId);
                dataToPage.put(RequestParameterName.TEST_DURATION, time);
                answer = gson.toJson(dataToPage);
            } else {
                LocalDateTime testEndTime = LocalDateTime.now();
                testService.completeTest(assignment, testEndTime);

                dataToPage.put(RequestParameterName.ASSIGNMENT_ID, assignment.getId());
                answer = gson.toJson(dataToPage);
            }
        } catch (TestLogServiceException | TestServiceException e) {
            response.setStatus(500);
            dataToPage.put(RequestParameterName.PAGE, JspPageName.ERROR_PAGE);
            return gson.toJson(dataToPage);
        } catch (TimeIsOverServiceException e) {
            logger.log(Level.ERROR, "Time is over in ShowQuestion command method execute()", e);
            dataToPage.put(RequestParameterName.TIME_IS_OVER, TRUE);
            dataToPage.put(RequestParameterName.ASSIGNMENT_ID, assignment.getId());
            return gson.toJson(dataToPage);
        } catch (InvalidTestKeyException e) {
            logger.log(Level.ERROR, "Invalid test key in ShowQuestion command method execute()", e);
            dataToPage.put(RequestParameterName.INVALID_KEY, TRUE);
            return gson.toJson(dataToPage);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in ShowQuestion command method execute()", e);
            response.setStatus(409);
            dataToPage.put(RequestParameterName.PAGE, JspPageName.ERROR_PAGE);
            return gson.toJson(dataToPage);
        }
        return answer;
    }
}
