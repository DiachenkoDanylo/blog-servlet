package com.diachenko.dietblog.dao.recipe;
/*  diet-blog
    16.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeDao {
    List<Recipe> getAllRecipes();

    Optional<Recipe> getRecipeById(int id);

    Recipe createRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    boolean deleteRecipe(int id);

    Optional<Recipe> getRecipeByTitle(String title);

    List<Recipe> getAllRecipesByUserId(int id);
}
