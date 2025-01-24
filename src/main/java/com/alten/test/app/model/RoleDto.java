package com.alten.test.app.model;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record RoleDto(
        String name
) implements Serializable {
}
