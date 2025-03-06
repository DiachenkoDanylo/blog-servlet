package com.diachenko.dietblog.dao.mapper;

import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.model.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/
public class RecipeMapper implements RowMapper<Recipe> {

    @Override
    public Recipe map(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("recipe_id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setDescription(rs.getString("description"));
        recipe.setCalories(rs.getInt("calories"));
        recipe.setCreatedAt(rs.getObject("recipe_created_at", LocalDateTime.class));
        recipe.setTitleImage(rs.getString("image_url"));

        int userId = rs.getInt("user_id");
        if (userId != 0) {
            AppUser appUser = new AppUser(
                    userId,
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getObject("user_created_at", LocalDateTime.class),
                    rs.getString("user_image_url")
            );
            recipe.setOwner(appUser);
        }

        return recipe;
    }
}

