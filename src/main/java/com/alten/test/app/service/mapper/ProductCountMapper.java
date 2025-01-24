package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ProductCountDto;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.service.ProductService;
import com.alten.test.app.service.ProductServiceImpl;
import org.hibernate.annotations.Comment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ProductCountMapper {

    @Autowired
    protected ProductService productService;

    @Mappings({
            @Mapping(source = "productId", target = "product", qualifiedByName = "productIdToProduct"),
            @Mapping(target = "wishList", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    public abstract ProductCount mapToProductCount(ProductCountDto ProductCountDto);

    @Mappings({
            @Mapping(source = "product", target = "productId", qualifiedByName = "productToProductId")
    })
    public abstract ProductCountDto mapToProductCountDto(ProductCount ProductCount);

    public List<ProductCountDto> productCountsToProductCountDtos(Set<ProductCount> productCounts) {
        return productCounts.stream().map(this::mapToProductCountDto).toList();
    }

    public Set<ProductCount> productCountDtosToProductCounts(List<ProductCountDto> productCountDtos) {
        return productCountDtos.stream().map(this::mapToProductCount).collect(Collectors.toSet());
    }

    @Named("productIdToProduct")
    protected Product productIdToProduct(Long productId) {
        return productService.get(productId);
    }

    @Named("productToProductId")
    static Long productToProductId(Product product) {
        return product.getId();
    }

}