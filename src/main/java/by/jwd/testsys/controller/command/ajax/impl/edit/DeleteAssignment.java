package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.logic.TestService;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class DeleteAssignment implements AjaxCommand {


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        int assignment_id = Integer.parseInt(request.getParameter(RequestParameterName.ASSIGNMENT_ID));

        String answer = null;
        Map<String, Set<User>> assignmentResult;
        Gson gson = new Gson();

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        TestService testService = serviceFactory.getTestService();

        try {
            testService.deleteAssignment(assignment_id);
        } catch (ServiceException e) {
            response.setStatus(500);
        }

        return answer;
    }


}
