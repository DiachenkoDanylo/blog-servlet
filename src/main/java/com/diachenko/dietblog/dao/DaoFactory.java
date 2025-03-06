package com.diachenko.dietblog.dao;

/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.appUser.AppUserDao;
import com.diachenko.dietblog.dao.appUser.AppUserDaoImpl;
import com.diachenko.dietblog.dao.recipe.RecipeDao;
import com.diachenko.dietblog.dao.recipe.RecipeDaoImpl;

import java.sql.Connection;

public class DaoFactory {
    private final Connection connection;

    public DaoFactory(Connection connection) {
        this.connection = connection;
    }

    public RecipeDao getRecipeDao() {
        return new RecipeDaoImpl(connection);
    }

    public AppUserDao getAppUserDao() {
        return new AppUserDaoImpl(connection);
    }

}
