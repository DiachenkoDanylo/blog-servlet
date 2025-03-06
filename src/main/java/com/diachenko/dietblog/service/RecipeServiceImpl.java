package com.diachenko.dietblog.service;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.recipe.RecipeDao;
import com.diachenko.dietblog.exception.DatabaseException;
import com.diachenko.dietblog.exception.RecipeNotFoundException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.model.Recipe;

import java.util.List;

public class RecipeServiceImpl implements SimpleService<Recipe> {

    private final RecipeDao recipeDao;

    public RecipeServiceImpl(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Override
    public Recipe save(Recipe recipe) {
        try {
            return recipeDao.createRecipe(recipe);
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Recipe findById(int id) {
        try {
            return recipeDao.getRecipeById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe with id: " + id + " not found"));
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Recipe> findAll() {
        try {
            return recipeDao.getAllRecipes();
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            return recipeDao.deleteRecipe(id);
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Recipe findByTitle(String title) {
        try {
            return recipeDao.getRecipeByTitle(title).orElseThrow(() -> new RecipeNotFoundException("Recipe with title: " + title + " not found"));
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Recipe updateRecipeById(Recipe recipe) {
        try {
            findById(recipe.getId());
            recipeDao.updateRecipe(recipe);
            return findById(recipe.getId());
        } catch (DatabaseException e) {
            throw new ServiceException("Error during updating recipe with id: " + recipe.getId() + " \n" + e.getMessage());
        }
    }

    public List<Recipe> findAllByUserId(int authorId) {
        try {
            List<Recipe> recipeList = recipeDao.getAllRecipesByUserId(authorId);
            if (recipeList.isEmpty()) {
                throw new RecipeNotFoundException("Recipes by author with id :" + authorId + " not found");
            } else {
                return recipeList;
            }
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
