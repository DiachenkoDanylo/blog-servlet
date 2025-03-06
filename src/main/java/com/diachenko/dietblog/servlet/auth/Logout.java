package com.diachenko.dietblog.servlet.auth;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.servlet.BaseServlet;
import com.diachenko.dietblog.utils.IPService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/logout")
public class Logout extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            AppUser user = (AppUser) req.getSession().getAttribute("user");
            IPService.logClientIpWithActivity(req, "Logout with email : " + user.getEmail());
            resp.sendRedirect("recipes");
        } catch (Exception e) {
            IPService.logClientIpWithActivity(req, "Failed attempt to logout");
            log.error("Unexpected error during logout for ip={}", IPService.getClientIp(req));
            resp.sendRedirect("index.jsp");
        } finally {
            req.getSession().invalidate();
        }
    }
}
