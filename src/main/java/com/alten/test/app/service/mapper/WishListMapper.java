package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ProductCountDto;
import com.alten.test.app.model.WishListDto;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.repository.domain.WishList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    @Mapping(source = "productCounts", target = "productCountDtos", qualifiedByName = "productCountsToProductCountDtos")
    WishListDto mapToWishListDto(WishList wishList);

    @Mapping(source = "productCountDtos", target = "productCounts", qualifiedByName = "productCountDtosToProductCounts")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "id", ignore = true)
    WishList mapToWishList(WishListDto wishListDto);

    @Named("productCountsToProductCountDtos")
    static List<ProductCountDto> productCountsToProductCountDtos(Set<ProductCount> productCounts) {
        return productCounts.stream().map(productCount -> Mappers.getMapper(ProductCountMapper.class).mapToProductCountDto(productCount)).toList();
    }

    @Named("productCountDtosToProductCounts")
    static Set<ProductCount> productCountDtosToProductCounts(List<ProductCountDto> productCountDtos) {
        return productCountDtos.stream().map(productCountDto -> Mappers.getMapper(ProductCountMapper.class).mapToProductCount(productCountDto)).collect(Collectors.toSet());
    }
}