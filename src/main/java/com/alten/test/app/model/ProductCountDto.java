package com.alten.test.app.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProductCountDto implements Serializable {

    private Long productId;

    private Integer quantity;

}
