package com.diachenko.dietblog.service;
/*  diet-blog
    17.02.2025
    @author DiachenkoDanylo
*/


import com.diachenko.dietblog.dao.appUser.AppUserDao;
import com.diachenko.dietblog.exception.DatabaseException;
import com.diachenko.dietblog.exception.DuplicateObjectException;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AppUserServiceImpl implements SimpleService<AppUser> {

    private final AppUserDao appUserDao;

    public AppUserServiceImpl(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    @Override
    public AppUser save(AppUser appUser) {
        Optional<AppUser> existingUser;

        try {
            existingUser = appUserDao.getAppUserByEmail(appUser.getEmail());
        } catch (DatabaseException e) {
            log.error("Error checking existing user by email: {}", appUser.getEmail(), e);
            throw new ServiceException("Database error occurred while checking user existence. \n " + e.getMessage());
        }

        if (existingUser.isPresent()) {
            log.warn("Attempt to create duplicate user with email: {}", appUser.getEmail());
            throw new DuplicateObjectException("User with email " + appUser.getEmail() + " already exists.");
        }

        try {
            return appUserDao.createAppUser(appUser);
        } catch (DatabaseException e) {
            log.error("Error saving user: {}", appUser.getEmail(), e);
            throw new ServiceException("Database error occurred while saving user. " + e.getMessage());
        }
    }

    @Override
    public AppUser findById(int id) {
        try {
            return appUserDao.getAppUserById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        } catch (DatabaseException e) {
            log.error("Error retrieving user with ID: {}", id, e);
            throw new ServiceException("Database error occurred while fetching user. " + e.getMessage());
        }
    }

    public AppUser findByEmail(String email) {
        try {
            return appUserDao.getAppUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<AppUser> findAll() {
        try {
            return appUserDao.getAllAppUsers();
        } catch (DatabaseException e) {
            throw new ServiceException("Database error occurred while fetching authors " + e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try {
            if (appUserDao.deleteAppUser(id)) {
                return true;
            } else {
                throw new ServiceException("Failed attempt to delete appuser with id = " + id);
            }
        } catch (DatabaseException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AppUser updateAppUserById(AppUser appUser) {
        try {
            findById(appUser.getId());
            appUserDao.updateAppUser(appUser);
            return findById(appUser.getId());
        } catch (DatabaseException e) {
            throw new ServiceException("Error during updating recipe with id: " + appUser.getId() + " \n" + e.getMessage());
        }
    }
}
