package com.diachenko.dietblog.servlet.author;

/*  diet-blog
    21.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.servlet.BaseServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet("/cabinet")
public class Cabinet extends BaseServlet {
    private RecipeServiceImpl recipeService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        try {
            AppUser appUser = (AppUser) session.getAttribute("user");
            List<Recipe> recipeList = recipeService.findAllByUserId(appUser.getId());
            req.setAttribute("recipes", recipeList);
            req.getRequestDispatcher("cabinet.jsp").forward(req, resp);
        } catch (RecipeNotFoundException e) {
            AppUser appUser = (AppUser) session.getAttribute("user");
            log.warn("owner recipes not found email={}", appUser.getEmail());
            req.getRequestDispatcher("cabinet.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
