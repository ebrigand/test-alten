package com.alten.test.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.ListResourceBundle;

public record AccountDto(
        String username,
        String firstname,
        String email,
        String password,
        @JsonProperty("roles") List<RoleDto> roleDtos
) implements Serializable {
}
