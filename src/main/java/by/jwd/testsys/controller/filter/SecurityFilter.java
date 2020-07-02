package by.jwd.testsys.controller.filter;

import by.jwd.testsys.controller.command.front.CommandName;
import by.jwd.testsys.controller.parameter.JspPageName;
import by.jwd.testsys.controller.parameter.RequestParameterName;
import by.jwd.testsys.controller.parameter.SessionAttributeName;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Sets character encoding for every request.
 */

@WebFilter(urlPatterns = {"/test"}, dispatcherTypes = DispatcherType.REQUEST)
public class SecurityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();

        String parameter1 = httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME);

        if (httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME) == null) {
            session.setAttribute(SessionAttributeName.SECURITY, SessionAttributeName.SECURITY);
        }
//todo
        String parameter = httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME);

        if (session != null && session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE) == null
                && !httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME).
                equals(CommandName.SIGN_IN.toString().toLowerCase())
                && !httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME).
                equals(CommandName.SIGN_UP.toString().toLowerCase())
                && !httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME).
                equals(CommandName.CHANGE_LANGUAGE.toString().toLowerCase())) {

            session.setAttribute(SessionAttributeName.SECURITY, SessionAttributeName.SECURITY);
        }
        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
