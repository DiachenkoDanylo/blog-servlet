package com.diachenko.dietblog.dao.recipe;

import com.diachenko.dietblog.dao.BaseDao;
import com.diachenko.dietblog.dao.mapper.RecipeMapper;
import com.diachenko.dietblog.dao.mapper.RowMapper;
import com.diachenko.dietblog.model.Recipe;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

public class RecipeDaoImpl extends BaseDao<Recipe> implements RecipeDao {

    private static final String RECIPE_SELECTION_JOIN_USER = "SELECT r.id AS recipe_id, r.title, r.description, r.calories, r.user_id as recipe_user_id, r.created_at AS recipe_created_at, r.image_url AS image_url, au.id AS user_id, au.username, au.password_hash, au.email, au.role, au.created_at AS user_created_at, au.image_url AS user_image_url FROM recipe r JOIN public.app_user au ON r.user_id = au.id ";
    private static final String CREATE_RECIPE = "INSERT INTO recipe"
            + "  (title, description, calories, user_id, image_url) VALUES " +
            " (?, ?, ?, ?, ?);";
    private static final String UPDATE_RECIPE = "UPDATE recipe SET title = ?, description = ?, calories = ?, user_id = ? , image_url = ? WHERE id = ?";
    private static final String DELETE_RECIPE_BY_ID = "DELETE FROM recipe WHERE id = ?";

    private static final String SELECT_RECIPE_BY_TITLE = "SELECT * FROM recipe  WHERE title = ?";
    private static final String SELECT_ALL_RECIPES_BY_USER_ID = "SELECT * FROM recipe  WHERE user_id = ?";
    private static final String SELECT_ALL_RECIPES = "SELECT r.id AS recipe_id, r.title, r.description, r.calories, r.user_id as recipe_user_id, r.created_at AS recipe_created_at, r.image_url AS image_url, au.id AS user_id, au.username, au.password_hash, au.email, au.role, au.created_at AS user_created_at, au.image_url AS user_image_url FROM recipe r JOIN public.app_user au ON r.user_id = au.id";
    private final RowMapper<Recipe> mapper = new RecipeMapper();


    public RecipeDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return executeQuery(RECIPE_SELECTION_JOIN_USER, mapper);
    }

    @Override
    public List<Recipe> getAllRecipesByUserId(int id) {
        String sql = " WHERE au.id = ?;";
        return executeQuery(RECIPE_SELECTION_JOIN_USER + sql, mapper, id);
    }

    @Override
    public Optional<Recipe> getRecipeById(int id) {
        String sql = " WHERE r.id = ?; ";
        return Optional.ofNullable(executeQueryForSingle(RECIPE_SELECTION_JOIN_USER + sql, mapper, id));
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        int id = executeInsert(CREATE_RECIPE, recipe.getTitle(), recipe.getDescription(),
                recipe.getCalories(),
                recipe.getOwner().getId(),
                recipe.getTitleImage());
        return getRecipeById(id).get();
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        executeUpdate(UPDATE_RECIPE, recipe.getTitle(), recipe.getDescription(),
                recipe.getCalories(), recipe.getOwner().getId(), recipe.getTitleImage(), recipe.getId());

    }

    @Override
    public boolean deleteRecipe(int id) {
        return executeDelete(DELETE_RECIPE_BY_ID, id);
    }

    @Override
    public Optional<Recipe> getRecipeByTitle(String title) {
        String sql = " WHERE r.title = ?; ";
        return Optional.ofNullable(executeQueryForSingle(RECIPE_SELECTION_JOIN_USER + sql, mapper, title));
    }

}
