package com.diachenko.dietblog.utils;
/*  diet-blog
    04.03.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AuthenticationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI();

        if (path.matches(".*/(edit|create|cabinet)$") || req.getMethod().equals("DELETE")) {

            try {
                boolean isLoggedIn = (session != null && session.getAttribute("user") != null)
                        && authenticationService.isUserAuthenticateValid((AppUser) session.getAttribute("user"));
                if (!isLoggedIn) {
                    resp.sendRedirect("/login");
                }
            } catch (UnauthorizedAccessException e) {
                IPService.logClientIpWithException(req, e);
                resp.sendRedirect("/login");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
