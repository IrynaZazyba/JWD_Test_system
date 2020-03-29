package by.jwd.testsys.logic.ajaxCommand.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.SessionAttributeName;
import by.jwd.testsys.logic.ajaxCommand.AjaxCommand;
import by.jwd.testsys.logic.logicCommand.CommandException;
import by.jwd.testsys.logic.service.ServiceException;
import by.jwd.testsys.logic.service.UserService;
import by.jwd.testsys.logic.service.factory.ServiceFactory;
import by.jwd.testsys.logic.util.Role;
import by.jwd.testsys.logic.validator.impl.UserValidatorImpl;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class EditUser implements AjaxCommand {

    private static Logger logger = LogManager.getLogger();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String answer = null;

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        User user = new User(login, password, first_name, last_name);

        UserService userService = ServiceFactory.getInstance().getUserService();
        HttpSession session = request.getSession(false);


        user.setId((Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE));
        user.setRole((Role) session.getAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE));
        String locale = (String) session.getAttribute("local");

        UserValidatorImpl userValidator = new UserValidatorImpl(user, locale);
        Map<String, String> userValidateAnswer = userValidator.validate();

        if (userValidateAnswer.size() != 0) {
            Gson gson = new Gson();
            userValidateAnswer.put("status", "error");
            answer = gson.toJson(userValidateAnswer);

        } else {

            try {
                User editedUser = userService.editUserInfo(user);
                if (editedUser != null) {
                    answer = "{\"message\":\"Data was changed successfully.\",\"status\":\"ok\"}";
                } else {
                    answer = "{\"message\":\"Data wasn't changed.\",\"status\":\"error\"}";
                }


            } catch (ServiceException e) {
                logger.log(Level.ERROR, e.getMessage());
                answer = "{\"message\":\"Data wasn't changed.\",\"status\":\"error\"}";
            }

        }

        return answer;
    }



}
