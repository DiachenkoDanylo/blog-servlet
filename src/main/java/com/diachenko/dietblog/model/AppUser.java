package com.diachenko.dietblog.model;
/*  diet-blog
    16.02.2025
    @author DiachenkoDanylo
*/


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppUser {

    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private String image = "";

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        AppUser appUser = (AppUser) object;
        return id == appUser.id && Objects.equals(username, appUser.username) && Objects.equals(passwordHash, appUser.passwordHash) && Objects.equals(email, appUser.email) && Objects.equals(role, appUser.role) && Objects.equals(createdAt, appUser.createdAt) && Objects.equals(image, appUser.image);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(passwordHash);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(image);
        return result;
    }
}
