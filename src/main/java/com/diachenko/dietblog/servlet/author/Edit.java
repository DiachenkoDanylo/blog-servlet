package com.diachenko.dietblog.servlet.author;
/*  diet-blog
    02.03.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.service.image.ImageStorageFactory;
import com.diachenko.dietblog.service.image.ImageStorageService;
import com.diachenko.dietblog.servlet.BaseServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet("/cabinet/edit")
public class Edit extends BaseServlet {

    private AuthenticationService authenticationService;
    private AppUserServiceImpl appUserService;
    private ImageStorageService storageService;

    @Override
    public void init() throws ServletException {
        super.init();
        authenticationService = new AuthenticationService();
        appUserService = new AppUserServiceImpl(daoFactory.getAppUserDao());
        storageService = ImageStorageFactory.getStorageService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        AppUser appUser = (AppUser) session.getAttribute("user");
        authenticationService.isUserAuthenticateValid(appUser);
        req.getRequestDispatcher("/cabinet-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        HttpSession session = req.getSession();
        AppUser appUser = (AppUser) session.getAttribute("user");
        Part filePart = req.getPart("image");

        try {
            AppUser toEdit = appUserService.findById(id);
            String imageUrl = toEdit.getImage();
            if (filePart != null && filePart.getSize() > 0) {
                imageUrl = storageService.saveImagePart(filePart);
            }
            AppUser appUserUpdated = AppUser.builder()
                    .id(id)
                    .username(username)
                    .email(email)
                    .createdAt(appUser.getCreatedAt())
                    .image(imageUrl)
                    .passwordHash(appUser.getPasswordHash())
                    .role(appUser.getRole())
                    .build();
            appUserUpdated = appUserService.updateAppUserById(appUserUpdated);
            session.setAttribute("user", appUserUpdated);
            resp.sendRedirect("/cabinet");
        } catch (ServiceException e) {
            log.error("Error during attempt to PUT edit appuser with id={} form userId={}", id, appUser.getId() + e.getMessage());
            resp.sendRedirect("/cabinet-edit.jsp?error=Server error");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
