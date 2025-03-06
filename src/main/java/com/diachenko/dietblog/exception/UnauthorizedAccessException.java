package com.diachenko.dietblog.exception;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
