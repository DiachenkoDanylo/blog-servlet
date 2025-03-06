package com.diachenko.dietblog.dao.mapper;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.model.AppUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppUserMapper implements RowMapper<AppUser> {

    @Override
    public AppUser map(ResultSet rs) throws SQLException {
        return new AppUser(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getObject("user_created_at", LocalDateTime.class),
                rs.getString("user_image_url")
        );
    }
}
