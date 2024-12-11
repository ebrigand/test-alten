package com.alten.test.app.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(String password) {
        super("Incorrect password: " + password);
    }

}
