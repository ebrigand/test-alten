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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ShoppingCartMapper {

    @Autowired
    protected ProductService productService;

    @Mappings({
        @Mapping(source = "products", target = "productIds", qualifiedByName = "productsToProductIds")
    })
    public abstract ShoppingCartDto mapToShoppingCartDto(ShoppingCart shoppingCart);

    @Mappings({
        @Mapping(source = "productIds", target = "products", qualifiedByName = "productIdsToProducts"),
        @Mapping(target = "account", ignore = true),
        @Mapping(target = "id", ignore = true)
    })
    public abstract ShoppingCart mapToShoppingCart(ShoppingCartDto shoppingCartDto);

    @Named("productIdsToProducts")
    Set<Product> productIdsToProducts(List<Long> productIds) {
        return productIds.stream().map(productId -> productService.get(productId)).collect(Collectors.toSet());
    }

    @Named("productsToProductIds")
    static List<Long> productsToProductIds(Set<Product> products) {
        return products.stream().map(Product::getId).toList();
    }
}