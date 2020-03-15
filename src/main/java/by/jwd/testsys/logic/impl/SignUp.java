package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.dao.factory.DAOType;
import by.jwd.testsys.logic.Command;
import by.jwd.testsys.logic.exception.CommandException;
import by.jwd.testsys.service.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUp implements Command {

    private static final String USER_ID_SESSION_ATTRIBUTE = "user_id";
    private static final String USER_LOGIN_SESSION_ATTRIBUTE = "user_login";
    private static final String USER_ROLE_SESSION_ATTRIBUTE = "user_role";

    private static final String USER_LOGIN_PARAMETER="login";
    private static final String USER_PASSWORD_PARAMETER="password";
    private static final String USER_FIRST_NAME_PARAMETER="first_name";
    private static final String USER_LAST_NAME_PARAMETER="last_name";

    private static final String FORWARD_HOME_URL="jsp/startMenu.jsp";
    private static final String FORWARD_ERROR_URL="jsp/errorPage.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String login = request.getParameter(USER_LOGIN_PARAMETER);
        String password = request.getParameter(USER_PASSWORD_PARAMETER);
        String firstName = request.getParameter(USER_FIRST_NAME_PARAMETER);
        String lastName = request.getParameter(USER_LAST_NAME_PARAMETER);

        DAOFactory daoFactory = DAOFactoryProvider.getFactory(DAOType.SQL);
        UserDAO userDao = daoFactory.getUserDao();

        try {
            User user=new User(login, password, firstName, lastName, Role.USER);
            userDao.save(user);
            HttpSession session = request.getSession();
            session.setAttribute(USER_ID_SESSION_ATTRIBUTE, user.getId());
            session.setAttribute(USER_LOGIN_SESSION_ATTRIBUTE,user.getLogin());
            session.setAttribute(USER_ROLE_SESSION_ATTRIBUTE, user.getRole());
            Command.forwardToPage(request,response,FORWARD_HOME_URL);

        } catch (DAOException e) {
            e.printStackTrace();
            Command.forwardToPage(request,response,FORWARD_ERROR_URL);
        }
    }
}
