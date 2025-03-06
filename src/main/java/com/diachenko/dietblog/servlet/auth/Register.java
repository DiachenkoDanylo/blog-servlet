package com.diachenko.dietblog.servlet.auth;

import com.diachenko.dietblog.exception.DuplicateObjectException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.servlet.BaseServlet;
import com.diachenko.dietblog.utils.IPService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

@Slf4j
@WebServlet("/register")
public class Register extends BaseServlet {

    private AppUserServiceImpl appUserService;
    private AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        appUserService = new AppUserServiceImpl(daoFactory.getAppUserDao());
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        try {
            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPasswordHash(authService.convertPassword(password));
            appUser.setEmail(email);
            appUser.setRole("user");
            appUser.setCreatedAt(LocalDateTime.now());
            AppUser createdUser = appUserService.save(appUser);
            req.getSession().setAttribute("user", createdUser);
            resp.sendRedirect("/");
        } catch (DuplicateObjectException e) {
            IPService.logClientIpWithException(req, e);
            log.error("Register attempt with duplicate email={}", email);
            resp.sendRedirect("auth/login.jsp?error = Please try login! Email was already in use");
        } catch (ServiceException e) {
            log.error("Error during register attempt with email={}", email);
            resp.sendRedirect("auth/register.jsp?error = Please try again later!");
        }
    }

}
