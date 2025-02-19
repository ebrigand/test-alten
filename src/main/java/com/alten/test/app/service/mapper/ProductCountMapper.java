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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductMapper.class)
public interface ProductCountMapper {

    @Mapping(source = "productDto", target = "product")
    @Mapping(target = "wishList", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductCount mapToProductCount(ProductCountDto ProductCountDto);

    @Mapping(source = "product", target = "productDto")
    ProductCountDto mapToProductCountDto(ProductCount ProductCount);

}