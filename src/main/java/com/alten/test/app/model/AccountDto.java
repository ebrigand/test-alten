package com.alten.test.app.model;

import java.io.Serializable;

public record AccountDto(
        String username,
        String firstname,
        String email,
        String password
) implements Serializable {
}
