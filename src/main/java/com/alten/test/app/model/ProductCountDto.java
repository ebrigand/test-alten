package com.alten.test.app.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


public record ProductCountDto(
        ProductDto productDto,
        Integer quantity
) implements Serializable {
}
