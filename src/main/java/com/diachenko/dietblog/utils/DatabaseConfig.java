package com.diachenko.dietblog.utils;
/*  diet-blog
    16.02.2025
    @author DiachenkoDanylo
*/


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:postgresql://localhost:5433/diet-blog";
    private static final String URL_DOCKER = "jdbc:postgresql://172.17.0.1:5433/diet-blog";
    private static final String USER = "postgres";
    private static final String PASSWORD = "diachenko";
    private static HikariDataSource dataSource;
    private static DataSource testDataSource;

    public static void setTestDataSource(DataSource ds) {
        testDataSource = ds;
    }

    private static HikariDataSource initProductionDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL_DOCKER);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(20);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public static Connection createConnection() {
        try {
            if (testDataSource != null) {
                return testDataSource.getConnection();
            }
            return initProductionDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting a connection", e);
        }
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(URL_DOCKER, USER, PASSWORD)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }
}
