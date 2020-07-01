package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


public class ChangePassword implements AjaxCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String newPassword = request.getParameter(RequestParameterName.USER_PASSWORD_PARAMETER);
        String oldPassword = request.getParameter(RequestParameterName.USER_OLD_PASSWORD_PARAMETER);

        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        UserService userService = serviceFactory.getUserService();

        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);
        String locale = (String) session.getAttribute(SessionAttributeName.LANGUAGE_ATTRIBUTE);

        String answer = null;
        Gson gson = new Gson();

        Map<String, String> mapAnswer = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("local/local", Locale.forLanguageTag(locale));

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            mapAnswer.put("message", bundle.getString("message.json.user_edit_changed"));
            mapAnswer.put("status", "ok");
            answer = gson.toJson(mapAnswer);

        } catch (InvalidUserDataException e) {
            for (String mess : e.getInvalidData()) {
                if (mess.equals(InvalidParam.INVALID_PASSWORD.toString())) {
                    mapAnswer.put(InvalidParam.INVALID_LOGIN.toString().toLowerCase(), bundle.getString("message.invalid_password"));
                }

                if (mess.equals(InvalidParam.PASSWORD_MISMATCH.toString())) {
                    mapAnswer.put(InvalidParam.INVALID_LOGIN.toString().toLowerCase(), bundle.getString("message.password_mismatch"));
                }
            }
            answer = gson.toJson(mapAnswer);
        } catch (ServiceException e) {
            response.setStatus(500);
        }

        return answer;

    }

}