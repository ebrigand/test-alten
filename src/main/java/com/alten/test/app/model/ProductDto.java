package com.alten.test.app.model;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

public record ProductDto(
        Long id,
        String code,
        String name,
        String description,
        String image,
        String category,
        Long price,
        Long quantity,
        String internalReference,
        Long shellId,
        @JsonProperty("inventoryStatus") InventoryStatusEnum inventoryStatusEnum,
        Long rating,
        Long createdAt,
        Long updatedAt
) implements Serializable {
}
