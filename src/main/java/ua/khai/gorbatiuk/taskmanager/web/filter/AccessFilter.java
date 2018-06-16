package ua.khai.gorbatiuk.taskmanager.web.filter;

import ua.khai.gorbatiuk.taskmanager.util.constant.RequestParameter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AccessFilter implements Filter{

    private static final String URL_START = "http://localhost:8080/";

    private Set<String> freeResources;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        freeResources = new HashSet<>();
        freeResources.add("login");
        freeResources.add("registration");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest= (HttpServletRequest)servletRequest;
        String url = httpRequest.getRequestURL().toString();
        String[] path = url.split(URL_START);
        if(path.length == 0 || freeResources.contains(path[1]) ||
                httpRequest.getSession().getAttribute(RequestParameter.CURRENT_USER) != null) {
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            httpRequest.getRequestDispatcher("jsp/login.jsp").forward(httpRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
