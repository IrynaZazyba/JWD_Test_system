package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.factory.ServiceFactory;
import by.jwd.testsys.bean.Role;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class EditUser implements AjaxCommand {

    private static Logger logger = LogManager.getLogger();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String answer;

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String password = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String first_name = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String last_name = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);
        String email = request.getParameter(RequestParameterName.USER_EMAIL_PARAMETER);

        User user = new User.Builder()
                .withLogin(login)
                .withPassword(password)
                .withFirstName(first_name)
                .withLastName(last_name)
                .withEmail(email)
                .build();

        UserService userService = ServiceFactory.getInstance().getUserService();
        HttpSession session = request.getSession(false);


        user.setId((Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE));
        user.setRole((Role) session.getAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE));
        String locale = (String) session.getAttribute(SessionAttributeName.LANGUAGE_ATTRIBUTE);


        ResourceBundle resourceBundle = ResourceBundle.getBundle("local/local", Locale.forLanguageTag(locale));


        try {
            User editedUser = userService.editUserInfo(user);
            if (editedUser != null) {
                answer = "{\"message\":\"" + resourceBundle.getString("message.json.user_edit_changed") +
                        "\",\"status\":\"ok\"}";
            }

            //todo exist login in exception also
            else {
                answer = "{\"message\":\"" + resourceBundle.getString("message.json.user_edit_error") +
                        "\",\"status\":\"error\"}";
            }
        } catch (InvalidUserDataException e) {
            Gson gson = new Gson();
            Set<String> invalidUserData = e.getInvalidData();
            Map<String, String> answerInvalidData = buildAnswer(invalidUserData, resourceBundle);
            answerInvalidData.put("status", "error");
            answer = gson.toJson(answerInvalidData);

        } catch (ServiceException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            answer = "{\"message\":\"" + resourceBundle.getString("message.json.user_edit_error") +
                    "\",\"status\":\"error\"}";
        }


        return answer;
    }

    private Map<String, String> buildAnswer(Set<String> params, ResourceBundle bundle) {

        Map<String, String> answer = new HashMap<>();
        for (String param : params) {
            if (param.equals(InvalidParam.INVALID_LOGIN.toString())) {
                answer.put(InvalidParam.INVALID_LOGIN.toString().toLowerCase(), bundle.getString("message.invalid_login"));
            }

            if (param.equals(InvalidParam.INVALID_PASSWORD.toString())) {
                answer.put(InvalidParam.INVALID_LOGIN.toString().toLowerCase(), bundle.getString("message.invalid_password"));
            }

            if (param.equals((InvalidParam.INVALID_FIRST_NAME.toString()))) {
                answer.put(InvalidParam.INVALID_FIRST_NAME.toString().toLowerCase(), bundle.getString("message.invalid_first_name"));
            }

            if (param.equals(InvalidParam.INVALID_LAST_NAME.toString())) {
                answer.put(InvalidParam.INVALID_LAST_NAME.toString().toLowerCase(), bundle.getString("message.invalid_last_name"));
            }
        }
        return answer;
    }


}
