package com.diachenko.dietblog.dao.appUser;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.BaseDao;
import com.diachenko.dietblog.dao.mapper.AppUserMapper;
import com.diachenko.dietblog.dao.mapper.RowMapper;
import com.diachenko.dietblog.model.AppUser;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class AppUserDaoImpl extends BaseDao<AppUser> implements AppUserDao {

    private static final String CREATE_USER = "INSERT INTO app_user" + "  (username, password_hash, email, role, created_at, image_url) VALUES " +
            " (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM app_user  WHERE id = ?";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM app_user  WHERE email = ?";
    private static final String SELECT_ALL_USERS = "SELECT  au.id AS user_id, au.username, au.password_hash, au.email, au.role, au.created_at AS user_created_at, au.image_url AS user_image_url FROM app_user au";
    private static final String DELETE_USER = "DELETE FROM app_user WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE app_user SET username = ?, email= ?, image_url = ? where id = ?";
    private final RowMapper<AppUser> mapper = new AppUserMapper();

    public AppUserDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        return executeQuery(SELECT_ALL_USERS, mapper);
    }

    @Override
    public Optional<AppUser> getAppUserById(int id) {
        String sql = " WHERE au.id = ?";
        return Optional.ofNullable(executeQueryForSingle(SELECT_ALL_USERS + sql, mapper, id));
    }

    @Override
    public AppUser createAppUser(AppUser user) {
        if (user.getImage().isEmpty()) {
            user.setImage("uploads/default_icon.jpg");
        }
        int id = executeInsert(CREATE_USER,
                user.getUsername(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getImage());
        return getAppUserById(id).get();
    }

    @Override
    public void updateAppUser(AppUser appuser) {
        executeUpdate(UPDATE_USER,
                appuser.getUsername(),
                appuser.getEmail(),
                appuser.getImage(), appuser.getId());
    }

    @Override
    public boolean deleteAppUser(int id) {
        return executeDelete(DELETE_USER, id);
    }

    public Optional<AppUser> getAppUserByEmail(String email) {
        String sql = " WHERE au.email = ?";
        return Optional.ofNullable(executeQueryForSingle(SELECT_ALL_USERS + sql, mapper, email));
    }

}
