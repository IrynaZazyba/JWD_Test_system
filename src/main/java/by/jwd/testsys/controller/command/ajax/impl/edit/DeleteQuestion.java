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

public class DeleteQuestion implements AjaxCommand {

    private static Logger logger = LogManager.getLogger(DeleteQuestion.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int questionId = Integer.parseInt(request.getParameter(RequestParameterName.QUESTION_ID));

        String answer = null;
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        AdminService adminService = serviceFactory.getAdminService();

        try {
            adminService.deleteQuestionWithAnswers(questionId);
            response.setStatus(204);
        } catch (AdminServiceException e) {
            response.setStatus(500);
        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in DeleteQuestion command method execute()",e);
            response.setStatus(409);
        }

        return answer;
    }


}
