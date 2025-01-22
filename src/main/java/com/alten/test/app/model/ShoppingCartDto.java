package com.alten.test.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

public record ShoppingCartDto(
        @JsonProperty("productIds") List<Long> productIds
) implements Serializable {
}
