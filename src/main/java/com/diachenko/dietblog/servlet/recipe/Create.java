package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    19.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.AuthenticationService;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.service.image.ImageStorageFactory;
import com.diachenko.dietblog.service.image.ImageStorageService;
import com.diachenko.dietblog.servlet.BaseServlet;
import com.diachenko.dietblog.utils.IPService;
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
import java.time.LocalDateTime;

@Slf4j
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet("/recipes/create")
public class Create extends BaseServlet {

    private RecipeServiceImpl recipeService;
    private ImageStorageService storageService;
    private AuthenticationService authenticationService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
        storageService = ImageStorageFactory.getStorageService();
        authenticationService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AppUser appUser = (AppUser) req.getSession().getAttribute("user");
        try {
            authenticationService.isUserAuthenticateValid(appUser);
            req.getRequestDispatcher("/recipe/recipeCreate.jsp").forward(req, resp);
        } catch (UnauthorizedAccessException e) {
            log.warn("attempt to create recipe with INVALID authorization with email={}", appUser.getEmail());
            resp.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        HttpSession session = req.getSession();
        AppUser appUser = (AppUser) session.getAttribute("user");
        Part filePart = req.getPart("image");
        try {
            authenticationService.isUserAuthenticateValid(appUser);
            recipeService.findByTitle(title);
        } catch (RecipeNotFoundException e) {
            String imageUrl = null;
            try {
                imageUrl = storageService.saveImagePart(filePart);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            Recipe recipe = Recipe.builder().title(title).description(description).calories(calories).createdAt(LocalDateTime.now()).owner(appUser).titleImage(imageUrl).build();
            recipeService.save(recipe);
            resp.sendRedirect("/recipes");
        } catch (ServiceException e) {
            log.warn("Error during save attempt recipe with with author ={} title={} description={} calories={}", appUser.getEmail(), title, description, calories);
            resp.sendRedirect("/recipe/reciteCreate.jsp?error=Error during adding recipe");
        } catch (UnauthorizedAccessException e) {
            IPService.logClientIpWithActivity(req, "attempt to create recipe POST ");
            log.warn("unauthorized access attempt to create recipe POST with email={}", appUser.getEmail());
            resp.sendRedirect("/login");
        }
    }

}
