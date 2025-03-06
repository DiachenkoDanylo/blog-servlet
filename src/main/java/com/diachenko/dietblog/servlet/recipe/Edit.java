package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    23.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.RecipeServiceImpl;
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
@WebServlet("/recipes/edit")
public class Edit extends BaseServlet {

    private RecipeServiceImpl recipeService;
    private ImageStorageService storageService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
        storageService = ImageStorageFactory.getStorageService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String recipeIdOptional = req.getParameter("id");
        AppUser appUser = (AppUser) req.getSession(false).getAttribute("user");
        try {
            int recipeId = Integer.parseInt(recipeIdOptional);
            Recipe toEdit = recipeService.findById(recipeId);
            if (toEdit.getOwner().equals(appUser)) {
                req.setAttribute("recipe", toEdit);
                req.getRequestDispatcher("/recipe/edit.jsp").forward(req, resp);
            } else {
                log.warn("attempt to create recipe without authorization with email={}", appUser.getEmail());
                resp.sendRedirect("/recipeShow.jsp?error=Not allowed to edit recipe not from author");
            }
        } catch (ServiceException e) {
            log.error("Error during attempt to edit recipe with id={} form userId={}", recipeIdOptional, appUser.getId());
            resp.sendRedirect("/recipeShow.jsp?id=" + recipeIdOptional);
        } catch (RecipeNotFoundException e) {
            log.error("Attempt to edit recipe which was not found  with id={} form userId={}", recipeIdOptional, appUser.getId());
            resp.sendRedirect("/recipeShow.jsp?error=Recipe not found for editing");
        } catch (NumberFormatException e) {
            log.error("Attempt to edit recipe with INVALID id={} form userId={}", recipeIdOptional, appUser.getId());
            resp.sendRedirect("/recipeShow.jsp?error=Invalid recipe ID for editing");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        HttpSession session = req.getSession(false);
        AppUser appUser = (AppUser) session.getAttribute("user");
        Part filePart = req.getPart("image");

        try {
            Recipe toEdit = recipeService.findById(id);
            String imageUrl = toEdit.getTitleImage();
            if (filePart != null && filePart.getSize() > 0) {
                imageUrl = storageService.saveImagePart(filePart);
            }
            Recipe recipe = Recipe.builder().id(id).title(title).description(description).calories(calories).createdAt(toEdit.getCreatedAt()).owner(appUser).titleImage(imageUrl).build();
            recipeService.updateRecipeById(recipe);
            resp.sendRedirect("/recipes?id=" + recipe.getId());
        } catch (ServiceException e) {
            log.error("Error during attempt to PUT edit recipe with id={} form userId={}", id, appUser.getId() + e.getMessage());
            resp.sendRedirect("/recipes?id=" + id);
        } catch (RecipeNotFoundException e) {
            log.error("Attempt to PUT edit recipe which was not found  with id={} form userId={}", id, appUser.getId());
            resp.sendRedirect("/recipes?id=" + id);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
