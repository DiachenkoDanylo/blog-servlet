package com.diachenko.dietblog.dao.mapper;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    T map(ResultSet rs) throws SQLException;
}

