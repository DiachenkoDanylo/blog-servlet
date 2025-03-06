package com.diachenko.dietblog.utils;
/*  diet-blog
    16.02.2025
    @author DiachenkoDanylo
*/

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Running Flyway migrations");
        DatabaseConfig.migrate();
    }

}

