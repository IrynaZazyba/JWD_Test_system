package by.jwd.testsys.controller.command.ajax.impl.edit;

import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;
import by.jwd.testsys.logic.UserService;
import by.jwd.testsys.logic.exception.InvalidUserDataException;
import by.jwd.testsys.logic.exception.ServiceException;
import by.jwd.testsys.logic.factory.ServiceFactory;
import by.jwd.testsys.logic.impl.UserServiceImpl;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


public class ChangePassword implements AjaxCommand {

    private final static Logger logger = LogManager.getLogger(ChangePassword.class);
    private static final String MESSAGE_KEY = "message";

    private static final String LOCAL_FILE_NAME = "local";
    private static final String LOCAL_FILE_PACKAGE = "local";

    private static final String LOCAL_MESSAGE_SUCCESS_USER_EDIT = "message.json.user_edit_changed";
    private static final String LOCAL_MESSAGE_INVALID_PASSWORD = "message.invalid_password";
    private static final String LOCAL_MESSAGE_MISMATCH_PASSWORD = "message.password_mismatch";

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
        ResourceBundle bundle = ResourceBundle.
                getBundle(LOCAL_FILE_PACKAGE + File.separator + LOCAL_FILE_NAME, Locale.forLanguageTag(locale));

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            mapAnswer.put(MESSAGE_KEY, bundle.getString(LOCAL_MESSAGE_SUCCESS_USER_EDIT));
            answer = gson.toJson(mapAnswer);

        } catch (InvalidUserDataException e) {
            response.setStatus(409);
            logger.log(Level.ERROR, "Invalid user data in ChangePassword command method execute()", e);

            for (String mess : e.getInvalidData()) {
                if (mess.equals(InvalidParam.INVALID_PASSWORD.toString())) {
                    mapAnswer.put(MESSAGE_KEY, bundle.getString(LOCAL_MESSAGE_INVALID_PASSWORD));
                }

                if (mess.equals(InvalidParam.PASSWORD_MISMATCH.toString())) {
                    mapAnswer.put(MESSAGE_KEY, bundle.getString(LOCAL_MESSAGE_MISMATCH_PASSWORD));
                }
            }
            answer = gson.toJson(mapAnswer);
        } catch (ServiceException e) {
            response.setStatus(500);
        }

        return answer;

    }

}