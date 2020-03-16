package by.jwd.testsys.logic;


import by.jwd.testsys.dao.exception.DAOFactoryException;
import by.jwd.testsys.dao.factory.DAOFactory;
import by.jwd.testsys.dao.factory.DAOFactoryProvider;
import by.jwd.testsys.dao.factory.DAOType;
import by.jwd.testsys.logic.exception.CommandException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {

    void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;

    static DAOFactory getDAOFactory(DAOType type) throws CommandException {
        DAOFactory daoFactory;
        try {
            daoFactory = DAOFactoryProvider.getFactory(type);
        } catch (DAOFactoryException e) {
            throw new CommandException("DAOFactoryException in sign in command", e);
        }

        return daoFactory;
    }

    static void forwardToPage(HttpServletRequest request, HttpServletResponse response, String url) throws CommandException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new CommandException(e);
        }

    }

    static void redirectToPage(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}