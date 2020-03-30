package by.jwd.testsys.logic.ajax.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameters.SessionAttributeName;
import by.jwd.testsys.logic.ajax.AjaxCommand;
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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class EditUser implements AjaxCommand {

    private static Logger logger = LogManager.getLogger();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
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

        ResourceBundle resourceBundle = ResourceBundle.getBundle("local/local", Locale.forLanguageTag(locale));

        if (userValidateAnswer.size() != 0) {
            Gson gson = new Gson();
            userValidateAnswer.put("status", "error");
            answer = gson.toJson(userValidateAnswer);

        } else {

            try {
                User editedUser = userService.editUserInfo(user);
                if (editedUser != null) {
                    answer = "{\"message\":\""+resourceBundle.getString("message.json.user_edit_changed")+
                            "\",\"status\":\"ok\"}";

                } else {
                    answer = "{\"message\":\""+resourceBundle.getString("message.json.user_edit_error")+
                            "\",\"status\":\"error\"}";
                }


            } catch (ServiceException e) {
                logger.log(Level.ERROR, e.getMessage());
                answer = "{\"message\":\""+resourceBundle.getString("message.json.user_edit_error")+
                        "\",\"status\":\"error\"}";
            }

        }

        return answer;
    }


}
