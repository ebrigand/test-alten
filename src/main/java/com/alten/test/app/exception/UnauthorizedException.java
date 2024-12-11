package com.alten.test.app.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String email) {
        super("Unauthorized email " + email);
    }

}
