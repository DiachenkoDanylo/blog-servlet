package com.diachenko.dietblog.exception;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
