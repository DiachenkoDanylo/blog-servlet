package com.diachenko.dietblog.exception;
/*  diet-blog
    23.02.2025
    @author DiachenkoDanylo
*/

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
