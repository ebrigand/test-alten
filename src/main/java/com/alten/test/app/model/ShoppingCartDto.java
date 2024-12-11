package com.alten.test.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ShoppingCartDto implements Serializable {

    @JsonProperty("productIds")
    private List<Long> productIds;

}
