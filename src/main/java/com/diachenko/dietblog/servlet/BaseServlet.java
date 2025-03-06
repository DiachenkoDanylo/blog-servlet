package com.diachenko.dietblog.servlet;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/


import com.diachenko.dietblog.dao.DaoFactory;
import com.diachenko.dietblog.utils.DatabaseConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseServlet extends HttpServlet {
    protected DaoFactory daoFactory;
    protected Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DatabaseConfig.createConnection();
            daoFactory = new DaoFactory(connection);
        } catch (Exception e) {
            throw new ServletException("Error initializing database connection", e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
