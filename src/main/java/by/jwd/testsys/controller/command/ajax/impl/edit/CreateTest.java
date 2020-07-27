package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CreateTest implements AjaxCommand {

    private static Logger logger = LogManager.getLogger(CreateTest.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String testId = request.getParameter(RequestParameterName.TEST_ID);
        int typeId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_TYPE_ID));
        String testTitle = request.getParameter(RequestParameterName.TEST_TITLE);
        String testKey = request.getParameter(RequestParameterName.TEST_KEY);
        String duration = request.getParameter(RequestParameterName.TEST_DURATION);
        LocalTime testDuration = null;
        if (duration != null) {
            testDuration = LocalTime.parse(duration);
        }

        String answer = null;
        Map<String, Object> parameterMapForJson = new HashMap<>();
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();
        HttpSession session = request.getSession();

        try {

            if (testId != null) {
                adminService.updateTestData(Integer.parseInt(testId), typeId, testTitle, testKey, testDuration);
            }

            if (testId == null) {
                int createdTestId = adminService.createTest(typeId, testTitle, testKey, testDuration);
                parameterMapForJson.put(RequestParameterName.TEST_ID, createdTestId);
                answer = gson.toJson(parameterMapForJson);
            }

            session.setAttribute(SessionAttributeName.TYPE_ID, typeId);
            session.setAttribute(SessionAttributeName.TEST_TITLE, testTitle);
            session.setAttribute(SessionAttributeName.TEST_KEY, testKey);
            session.setAttribute(SessionAttributeName.TEST_DURATION,testDuration);

        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in CreateTest command method execute()",e);
            response.setStatus(500);
        }
        return answer;
    }
}
