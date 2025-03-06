package com.diachenko.dietblog.servlet.auth;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
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

@Slf4j
@WebServlet("/login")
public class Login extends BaseServlet {

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
        req.getRequestDispatcher("/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            AppUser appUser = appUserService.findByEmail(email);
            if (authService.authenticate(password, appUser)) {
                req.getSession().setAttribute("user", appUser);
                resp.sendRedirect("recipes");
            } else {
                IPService.logClientIpWithActivity(req, "Logging attempt failed with email : " + email);
                log.warn("Authentication failed for email={}", email);
                resp.sendRedirect("/auth/login.jsp?error=Invalid password or email");
            }
        } catch (UserNotFoundException e) {
            log.warn("Login attempt failed: User not found for email={}", email);
            resp.sendRedirect("/auth/login.jsp?error=User not found");
        } catch (ServiceException e) {
            log.error("Unexpected error during login for email={}", email, e);
            resp.sendRedirect("/auth/login.jsp?error=Server error");
        }
    }

}
