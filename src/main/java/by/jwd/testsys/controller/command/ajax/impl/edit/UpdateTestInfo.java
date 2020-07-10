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
import java.time.LocalTime;

public class UpdateTestInfo implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(UpdateTestInfo.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int testId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_ID));
        int typeId = Integer.parseInt(request.getParameter(RequestParameterName.TEST_TYPE_ID));
        String testTitle = request.getParameter(RequestParameterName.TEST_TITLE);
        LocalTime duration = LocalTime.parse(request.getParameter(RequestParameterName.TEST_DURATION));
        String testKey = request.getParameter(RequestParameterName.TEST_KEY);

        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.updateTestData(testId, typeId, testTitle, testKey, duration);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in UpdateTestInfo command method execute()");
            response.setStatus(500);
        }

        return answer;
    }


}
