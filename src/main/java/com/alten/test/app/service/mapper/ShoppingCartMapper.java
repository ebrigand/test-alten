package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ShoppingCartDto;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.repository.domain.ShoppingCart;
import com.alten.test.app.service.ProductService;
import com.alten.test.app.service.ProductServiceImpl;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = ProductMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "products", target = "productDtos")
    ShoppingCartDto mapToShoppingCartDto(ShoppingCart shoppingCart);

    @Mapping(source = "productDtos", target = "products")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "id", ignore = true)
    ShoppingCart mapToShoppingCart(ShoppingCartDto shoppingCartDto);

}