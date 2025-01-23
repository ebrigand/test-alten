package com.alten.test.app.service.mapper;

import com.alten.test.app.model.ProductCountDto;
import com.alten.test.app.model.WishListDto;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.repository.domain.WishList;
import com.alten.test.app.service.ProductService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class WishListMapper {

    @Autowired
    protected ProductCountMapper productCountMapper;
    
    @Mappings ({
        @Mapping(target = "productCountDtos", expression = "java(productCountMapper.productCountsToProductCountDtos(wishList.getProductCounts()))")
    })
    public abstract WishListDto mapToWishListDto(WishList wishList);

    @Mappings ({
        @Mapping(target = "productCounts", expression = "java(productCountMapper.productCountDtosToProductCounts(wishListDto.productCountDtos()))"),
        @Mapping(target = "account", ignore = true),
        @Mapping(target = "id", ignore = true)
    })
    public abstract WishList mapToWishList(WishListDto wishListDto);

}