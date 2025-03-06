package com.diachenko.dietblog.servlet;

import com.diachenko.dietblog.exception.DatabaseException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.RecipeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

@WebServlet("")
public class Main extends BaseServlet {
    private RecipeServiceImpl recipeService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Recipe> recipes = recipeService.findAll();
            req.setAttribute("recipes", recipes);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (ServiceException e) {
            req.getRequestDispatcher("/index.jsp?error=Server failed").forward(req, resp);
        }
    }
}
