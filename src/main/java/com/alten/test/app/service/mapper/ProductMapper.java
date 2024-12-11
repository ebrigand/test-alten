package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ProductDto;
import com.alten.test.app.repository.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto mapToProductDto(Product product);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product mapToProduct(ProductDto productDto);

    static Long map(LocalDateTime createdAt) {
        ZonedDateTime zdt = ZonedDateTime.of(createdAt, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }



}