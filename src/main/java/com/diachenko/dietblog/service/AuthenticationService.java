package com.diachenko.dietblog.service;
/*  diet-blog
    23.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.dao.DaoFactory;
import com.diachenko.dietblog.exception.ServiceException;
import com.diachenko.dietblog.exception.UnauthorizedAccessException;
import com.diachenko.dietblog.exception.UserNotFoundException;
import com.diachenko.dietblog.model.AppUser;
import com.diachenko.dietblog.utils.DatabaseConfig;
import com.diachenko.dietblog.utils.PasswordBCryptHasher;

import java.sql.Connection;

public class AuthenticationService {

    private final Connection connection = DatabaseConfig.createConnection();
    private final DaoFactory daoFactory = new DaoFactory(connection);
    private final AppUserServiceImpl appUserService = new AppUserServiceImpl(daoFactory.getAppUserDao());
    private final PasswordBCryptHasher cryptHasher = new PasswordBCryptHasher();

    public boolean authenticate(String password, AppUser appUser) {
        return cryptHasher.verifyPassword(password, appUser.getPasswordHash());
    }

    public boolean isUserAuthenticateValid(AppUser appUser) {
        try {
            AppUser appUser1 = appUserService.findByEmail(appUser.getEmail());
            return appUser.equals(appUser1);
        } catch (ServiceException | UserNotFoundException e) {
            throw new UnauthorizedAccessException("unauthorized access");
        }
    }

    public String convertPassword(String pass) {
        for (int i = 1; i < 10; i++) {
            String val = cryptHasher.hashPassword(pass + String.valueOf(i));
            System.out.println(pass+i);
            System.out.println("\n " + val + " \n");
        }
        return cryptHasher.hashPassword(pass);
    }
}
