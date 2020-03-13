package by.jwd.testsys.logic.impl;

import by.jwd.testsys.bean.User;
import by.jwd.testsys.dao.UserDAO;
import by.jwd.testsys.dao.exception.DAOException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.dao.factory.DAOType;
import by.jwd.testsys.logic.Command;
import by.jwd.testsys.logic.exception.CommandException;

import javax.servlet.http.HttpServletRequest;

public class SignIn implements Command {

    @Override
    public String execute(HttpServletRequest request) throws CommandException {

        String page = null;

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        DAOFactory daoFactory = DAOFactoryProvider.getFactory(DAOType.SQL);
        UserDAO userDao = daoFactory.getUserDao();
        page = "jsp/signInPage.jsp";

        try {
            User userByLogin = userDao.getUserByLogin(login);
            if (userByLogin.getPassword().equals(password)) {


            }
        } catch (DAOException e) {
            e.printStackTrace();
        }
        return page;
    }
}
