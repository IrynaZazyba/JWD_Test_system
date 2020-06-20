package by.jwd.testsys.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Sets content-type for every ajax response.
 */

@WebFilter(urlPatterns = {"/ajax"},
        initParams = {
        @WebInitParam(name = "content-type", value = "application/json", description = "Content type param")})
public class AjaxContentTypeFilter implements Filter {

    private String defaultContentType = "application/json";
    private static final String CONTENT_TYPE = "content-type";

    @Override
    public void init(FilterConfig filterConfig) {
        String contentType = filterConfig.getInitParameter(CONTENT_TYPE);
        if (contentType != null) {
            defaultContentType = contentType;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        servletResponse.setContentType(defaultContentType);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
