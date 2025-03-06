package com.diachenko.dietblog.utils;
/*  diet-blog
    18.02.2025
    @author DiachenkoDanylo
*/

import org.mindrot.jbcrypt.BCrypt;

public class PasswordBCryptHasher {

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean verifyPassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }
}
