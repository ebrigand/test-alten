package com.alten.test.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WantedListDto implements Serializable {

    @JsonProperty("productCounts")
    private List<ProductCountDto> productCountDtos;

}
