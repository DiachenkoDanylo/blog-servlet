package com.diachenko.dietblog.servlet.recipe;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.servlet.BaseServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/recipes")
public class Show extends BaseServlet {

    private RecipeServiceImpl recipeService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String recipeIdParam = req.getParameter("id");
        if (recipeIdParam != null) {
            handleRecipeById(req, resp, recipeIdParam);
        } else {
            handleAllRecipes(req, resp);
        }
    }

    private void handleRecipeById(HttpServletRequest req, HttpServletResponse resp, String recipeIdParam) throws IOException, ServletException {
        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            Recipe recipe = recipeService.findById(recipeId);
            req.setAttribute("recipe", recipe);
            req.getRequestDispatcher("/recipe/recipeShow.jsp").forward(req, resp);
        } catch (ServiceException e) {
            log.error("Error during showing recipe recipeId={}", recipeIdParam, e);
            resp.sendRedirect("/recipe/recipeShow.jsp?error=Error loading recipe. Please try again.");
        } catch (NumberFormatException e) {
            log.error("Attempt to show recipe with INVALID id={}", recipeIdParam);
            resp.sendRedirect("/recipe/recipeShow.jsp?error=Invalid recipe ID.");
        }
    }

    private void handleAllRecipes(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            List<Recipe> recipeList = recipeService.findAll();
            req.setAttribute("recipes", recipeList);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (ServiceException e) {
            log.error("Attempt to show all recipes", e);
            resp.sendRedirect("/index.jsp?error=Error loading recipes. Please try again later.");
        }
    }


}