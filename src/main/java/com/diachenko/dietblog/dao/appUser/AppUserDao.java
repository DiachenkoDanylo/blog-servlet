package com.diachenko.dietblog.dao.appUser;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

import com.diachenko.dietblog.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserDao {
    List<AppUser> getAllAppUsers();

    Optional<AppUser> getAppUserById(int id);

    Optional<AppUser> getAppUserByEmail(String email);

    AppUser createAppUser(AppUser appUser);

    void updateAppUser(AppUser appUser);

    boolean deleteAppUser(int id);
}
