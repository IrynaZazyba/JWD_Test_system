package by.jwd.testsys.logic.ajaxCommand.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.controller.JspPageName;
import by.jwd.testsys.controller.RequestParameterName;
import by.jwd.testsys.controller.SessionAttributeName;
import by.jwd.testsys.logic.ajaxCommand.AjaxCommand;
import by.jwd.testsys.logic.logicCommand.Command;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class EditUser implements AjaxCommand {

    private static final String JSP_PAGE_PATH = "WEB-INF/jsp/startMenu.jsp";
    private static Logger logger = LogManager.getLogger();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String answer = null;
        UserService userService = ServiceFactory.getInstance().getUserService();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        User user = new User(login, password, first_name, last_name);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(JSP_PAGE_PATH);
        HttpSession session = request.getSession(false);

        if (requestDispatcher != null && session != null) {

            user.setId((Integer) session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE));
            user.setRole((Role) session.getAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE));
            String locale = (String) session.getAttribute("local");

            UserValidatorImpl userValidator = new UserValidatorImpl(user, locale);
            Map<String, String> userValidateAnswer = userValidator.validate();

            if (userValidateAnswer.size() != 0) {
                Gson gson = new Gson();
                userValidateAnswer.put("status", "error");
                answer = gson.toJson(userValidateAnswer);
                request.setAttribute(RequestParameterName.SIGN_UP_ERROR, "error");

            } else {
                try {
                    User editedUser = userService.editUserInfo(user);
                    if (editedUser != null) {
                        answer = "{\"message\":\"Data was changed successfully.\",\"status\":\"ok\"}";
                    } else {
                        answer = "{\"message\":\"Data wasn't changed.\",\"status\":\"error\"}";
                    }


                } catch (ServiceException e) {
                    logger.log(Level.ERROR, "Service Exception in edit_user logicCommand.", e);
                    answer = "{\"message\":\"Data wasn't changed.\",\"status\":\"error\"}";
                }

            }
        } else {
            answer = "{\"message\":\"Data wasn't changed.\",\"status\":\"error\"}";
        }
        return answer;
    }
}
