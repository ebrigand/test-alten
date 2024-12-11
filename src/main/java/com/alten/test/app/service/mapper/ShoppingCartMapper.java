package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ShoppingCartDto;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.repository.domain.ShoppingCart;
import com.alten.test.app.service.ProductServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    @Mapping(source = "products", target = "productIds", qualifiedByName = "productsToProductIds")
    ShoppingCartDto mapToShoppingCartDto(ShoppingCart shoppingCart);

    @Mapping(source = "productIds", target = "products", qualifiedByName = "productIdsToProducts")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "id", ignore = true)
    ShoppingCart mapToShoppingCart(ShoppingCartDto shoppingCartDto);

    @Named("productIdsToProducts")
    static Set<Product> productIdsToProducts(List<Long> productIds) {
        return productIds.stream().map(productId -> ProductServiceImpl.get().get(productId)).collect(Collectors.toSet());
    }

    @Named("productsToProductIds")
    static List<Long> productsToProductIds(Set<Product> products) {
        return products.stream().map(Product::getId).toList();
    }
}