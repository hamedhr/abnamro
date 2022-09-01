package com.abnamro.recipe.exception;

public abstract class BaseException extends RuntimeException {
    BaseException(String message) {
        super(message);
    }
}
