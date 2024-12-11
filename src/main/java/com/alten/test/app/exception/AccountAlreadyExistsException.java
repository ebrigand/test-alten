package com.alten.test.app.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String username, String email) {
        super("username or email already exist email: " + email + " username: " + username);
    }

}
