package by.jwd.testsys.logic.command;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {

    void execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;


    static void forwardToPage(HttpServletRequest request, HttpServletResponse response, String url) throws CommandException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new CommandException("Exception in forward",e);
        }

    }

    static void redirectToPage(HttpServletResponse response, String url) throws CommandException {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new CommandException("Exception in redirect ",e);

        }

    }
}