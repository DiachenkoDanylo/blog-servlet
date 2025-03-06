package com.diachenko.dietblog.exception;
/*  diet-blog
    23.02.2025
    @author DiachenkoDanylo
*/

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
