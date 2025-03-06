package com.diachenko.dietblog.servlet.author;
/*  diet-blog
    19.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.service.AppUserServiceImpl;
import com.diachenko.dietblog.service.RecipeServiceImpl;
import com.diachenko.dietblog.servlet.BaseServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/authors")
public class Author extends BaseServlet {

    private RecipeServiceImpl recipeService;
    private AppUserServiceImpl appUserService;

    @Override
    public void init() throws ServletException {
        super.init();
        recipeService = new RecipeServiceImpl(daoFactory.getRecipeDao());
        appUserService = new AppUserServiceImpl(daoFactory.getAppUserDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorIdOptional = req.getParameter("id");
        if (authorIdOptional != null) {
            handleAuthorById(req, resp, authorIdOptional);
        } else {
            handleAllAuthors(req, resp);
        }
    }

    private void handleAuthorById(HttpServletRequest req, HttpServletResponse resp, String authorIdParam) throws IOException, ServletException {
        try {
            int authorId = Integer.parseInt(authorIdParam);
            req.setAttribute("recipes", recipeService.findAllByUserId(authorId));
            AppUser appUser = appUserService.findById(authorId);
            req.setAttribute("author", appUser);
            req.getRequestDispatcher("/recipe/recipeAuthorShow.jsp").forward(req, resp);
        } catch (RecipeNotFoundException e) {
            int authorId = Integer.parseInt(authorIdParam);
            AppUser appUser = appUserService.findById(authorId);
            req.setAttribute("author", appUser);
            req.getRequestDispatcher("/recipe/recipeAuthorShow.jsp").forward(req, resp);
        } catch (ServiceException e) {
            log.error("Error during handling author attempt with id={}", authorIdParam);
            resp.sendRedirect("/recipe/recipeAuthor.jsp?error=Error loading author. Please try again.");
        } catch (NumberFormatException e) {
            log.error("Error during handling author attempt with invalid author id={}", authorIdParam);
            resp.sendRedirect("/recipe/recipeAuthor.jsp?error=Invalid author ID");
        } catch (UserNotFoundException e) {
            log.warn("User not found  during handling author attempt with author id={}", authorIdParam);
            resp.sendRedirect("/recipe/recipeAuthor.jsp?error=User with this author ID not found");
        }
    }

    private void handleAllAuthors(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            req.setAttribute("authorsList", appUserService.findAll());
            req.getRequestDispatcher("/recipe/recipeAuthor.jsp").forward(req, resp);
        } catch (ServiceException e) {
            log.error("Error during handling all authors attempt");
            resp.sendRedirect("/index.jsp?error=Error loading authors. Please try again later.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
