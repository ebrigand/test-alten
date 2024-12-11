package com.alten.test.app.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryStatusEnum {

    INSTOCK("INSTOCK"),
    LOWSTOCK("LOWSTOCK"),
    OUTOFSTOCK("OUTOFSTOCK");

    private final String value;

    InventoryStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
