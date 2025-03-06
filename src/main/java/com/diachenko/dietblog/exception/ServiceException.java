package com.diachenko.dietblog.exception;
/*  diet-blog
    22.02.2025
    @author DiachenkoDanylo
*/

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
