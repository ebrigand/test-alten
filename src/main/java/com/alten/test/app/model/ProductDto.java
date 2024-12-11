package com.alten.test.app.model;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProductDto implements Serializable {
        private Long id;
        private String code;
        private String name;
        private String description;
        private String image;
        private String category;
        private Long price;
        private Long quantity;
        private String internalReference;
        private Long shellId;
        @JsonProperty("inventoryStatus")
        private InventoryStatusEnum inventoryStatusEnum;
        private Long rating;
        private Long createdAt;
        private Long updatedAt;
}
