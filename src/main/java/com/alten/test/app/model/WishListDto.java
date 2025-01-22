package com.alten.test.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record WishListDto(
        @JsonProperty("productCounts") List<ProductCountDto> productCountDtos
) implements Serializable {
}
