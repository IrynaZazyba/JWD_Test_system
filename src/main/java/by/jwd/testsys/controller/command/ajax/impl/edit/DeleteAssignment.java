package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.AdminService;
import by.jwd.testsys.logic.exception.AdminServiceException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAssignment implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(DeleteAssignment.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int assignment_id = Integer.parseInt(request.getParameter(RequestParameterName.ASSIGNMENT_ID));

        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.deleteAssignment(assignment_id);
            response.setStatus(204);

        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (ServiceException e) {
            response.setStatus(500);
            logger.log(Level.ERROR, "Invalid user data in DeleteAssignment command method execute()",e);
        }

        return answer;
    }


}
