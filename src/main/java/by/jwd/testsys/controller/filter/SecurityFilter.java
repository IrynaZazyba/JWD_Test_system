package by.jwd.testsys.controller.filter;

import by.jwd.testsys.bean.Role;
import by.jwd.testsys.controller.command.ajax.AjaxCommand;
import by.jwd.testsys.controller.command.ajax.AjaxCommandName;
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


@WebFilter(urlPatterns = {"/test", "/ajax"}, dispatcherTypes = DispatcherType.REQUEST)
public class SecurityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();

        String command = httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME);

        if (session == null || command == null) {
            sendRedirectToStartPage(servletResponse);
        }else

        if ( !command.equals(CommandName.SIGN_IN.toString().toLowerCase())
              && !command.equals(CommandName.SIGN_UP.toString().toLowerCase())
              && !command.equals(CommandName.CHANGE_LANGUAGE.toString().toLowerCase())) {

            Object userId = session.getAttribute(SessionAttributeName.USER_ID_SESSION_ATTRIBUTE);

            if (userId == null) {
                sendRedirectToStartPage(servletResponse);
            }

            if (userId != null) {
                String name = httpServletRequest.getParameter(RequestParameterName.COMMAND_NAME);

                Role role;

                try {
                    CommandName commandName = CommandName.valueOf(name.toUpperCase());
                    role = commandName.getRole();
                } catch (IllegalArgumentException ex) {
                    AjaxCommandName commandName = AjaxCommandName.valueOf(name.toUpperCase());
                    role = commandName.getRole();
                }

                String sessionRole = String.valueOf(session.getAttribute(SessionAttributeName.USER_ROLE_SESSION_ATTRIBUTE));


                if (sessionRole.equalsIgnoreCase(Role.USER.toString()) && role == Role.ADMIN) {
                    sendRedirectToStartPage(servletResponse);
                } else {
                    filterChain.doFilter(httpServletRequest, servletResponse);
                }
            }
        } else {
            filterChain.doFilter(httpServletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private void sendRedirectToStartPage(ServletResponse servletResponse) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.sendRedirect(JspPageName.START_PAGE);
    }
}
