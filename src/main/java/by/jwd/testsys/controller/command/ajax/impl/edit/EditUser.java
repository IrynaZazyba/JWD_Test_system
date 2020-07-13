package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.bean.Role;
import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.ExistsUserException;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.UserServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import by.jwd.testsys.logic.validator.util.InvalidParam;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

public class EditUser implements AjaxCommand {

    private static final Logger logger = LogManager.getLogger(EditUser.class);
    private static final String MESSAGE_KEY = "message";

    private static final String LOCAL_FILE_NAME = "local";
    private static final String LOCAL_FILE_PACKAGE = "local";
    private static final String LOCAL_MESSAGE_SUCCESS_USER_EDIT = "message.json.user_edit_changed";
    private static final String LOCAL_MESSAGE_EXISTS_LOGIN = "message.exists_login";
    private static final String LOCAL_MESSAGE_ERROR = "message.json.user_edit_error";

    private static final String LOCAL_MESSAGE_INVALID_LOGIN = "message.invalid_login";
    private static final String LOCAL_MESSAGE_INVALID_FIRST_NAME = "message.invalid_first_name";
    private static final String LOCAL_MESSAGE_INVALID_LAST_NAME = "message.invalid_last_name";
    private static final String LOCAL_MESSAGE_INVALID_EMAIL="message.invalid_email";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String answer;

        String login = request.getParameter(RequestParameterName.USER_LOGIN_PARAMETER);
        String first_name = request.getParameter(RequestParameterName.USER_FIRST_NAME_PARAMETER);
        String last_name = request.getParameter(RequestParameterName.USER_LAST_NAME_PARAMETER);
        String email = request.getParameter(RequestParameterName.USER_EMAIL_PARAMETER);

        User user = new User.Builder()
                .withLogin(login)
                .withFirstName(first_name)
                .withLastName(last_name)
                .withEmail(email)
                .build();

        UserService userService = ServiceFactory.getInstance().getUserService();
        HttpSession session = request.getSession(false);

        user.setId((Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE));
        user.setRole((Role) session.getAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE));
        String locale = (String) session.getAttribute(SessionAttributeName.LANGUAGE_ATTRIBUTE);


        ResourceBundle resourceBundle = ResourceBundle.
                getBundle(LOCAL_FILE_PACKAGE + File.separator + LOCAL_FILE_NAME, Locale.forLanguageTag(locale));
        Gson gson = new Gson();
        JsonObject answerToUser = new JsonObject();
        try {
            User editedUser = userService.editUserInfo(user);
            answerToUser.addProperty(MESSAGE_KEY, resourceBundle.getString(LOCAL_MESSAGE_SUCCESS_USER_EDIT));
            answer = answerToUser.toString();

        } catch (InvalidUserDataException e) {
            logger.log(Level.ERROR, "Invalid user data in EditUser command method execute()", e);
            response.setStatus(409);
            Set<String> invalidUserData = e.getInvalidData();
            Map<String, Set<String>> answerInvalidData = buildAnswer(invalidUserData, resourceBundle);
            answer = gson.toJson(answerInvalidData);
        } catch (UserServiceException e) {
            response.setStatus(500);
            answerToUser.addProperty(MESSAGE_KEY, resourceBundle.getString(LOCAL_MESSAGE_ERROR));
            answer = answerToUser.toString();
        } catch (ExistsUserException e) {
            answerToUser.addProperty(MESSAGE_KEY, resourceBundle.getString(LOCAL_MESSAGE_EXISTS_LOGIN));
            answer = answerToUser.toString();
            logger.log(Level.ERROR, "Exists user data in EditUser command method execute()", e);
            response.setStatus(409);
        }

        return answer;
    }

    private Map<String, Set<String>> buildAnswer(Set<String> params, ResourceBundle bundle) {

        Map<String, Set<String>> answer = new HashMap<>();
        Set<String> messages = new HashSet<>();
        for (String param : params) {
            if (param.equals(InvalidParam.INVALID_LOGIN.toString())) {
                messages.add(bundle.getString(LOCAL_MESSAGE_INVALID_LOGIN));
            }

            if (param.equals((InvalidParam.INVALID_FIRST_NAME.toString()))) {
                messages.add(bundle.getString(LOCAL_MESSAGE_INVALID_FIRST_NAME));
            }

            if (param.equals(InvalidParam.INVALID_LAST_NAME.toString())) {
                messages.add(bundle.getString(LOCAL_MESSAGE_INVALID_LAST_NAME));
            }

            if (param.equals(InvalidParam.INVALID_EMAIL.toString())) {
                messages.add(bundle.getString(LOCAL_MESSAGE_INVALID_EMAIL));
            }
        }
        answer.put(MESSAGE_KEY, messages);
        return answer;
    }


}
