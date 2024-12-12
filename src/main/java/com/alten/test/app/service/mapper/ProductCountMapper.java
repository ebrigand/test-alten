package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ProductCountDto;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.service.ProductServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductCountMapper {

    @Mapping(source = "productId", target = "product", qualifiedByName = "productIdToProduct")
    @Mapping(target = "wishList", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductCount mapToProductCount(ProductCountDto ProductCountDto);

    @Mapping(source = "product", target = "productId", qualifiedByName = "productToProductId")
    ProductCountDto mapToProductCountDto(ProductCount ProductCount);

    @Named("productIdToProduct")
    static Product productIdToProduct(Long productId) {
        return ProductServiceImpl.get().get(productId);
    }

    @Named("productToProductId")
    static Long productToProductId(Product product) {
        return product.getId();
    }

}