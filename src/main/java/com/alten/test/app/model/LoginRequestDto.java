package com.alten.test.app.model;

import java.io.Serializable;

public record LoginRequestDto(
        String email,
        String password
) implements Serializable {
}
