package com.alten.test.app.service;

import com.alten.test.app.model.ProductDto;
import com.alten.test.app.repository.domain.Product;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAll();
    ProductDto post(ProductDto newProductDto);
    Product get(Long id);
    ProductDto getProductDto(Long id);
    ProductDto patch(Long id, ProductDto patchProductDto);
    void delete(Long id);
}
