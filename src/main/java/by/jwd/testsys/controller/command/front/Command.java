package by.jwd.testsys.controller.command.front;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {

    void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    default void forwardToPage(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException, ForwardCommandException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        if (requestDispatcher != null) {
            requestDispatcher.forward(request, response);
        } else {
            //todo logger
            throw new ForwardCommandException("Exception in forwardToPage() requestDispatcher=null.");
        }
    }

}