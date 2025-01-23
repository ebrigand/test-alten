package com.alten.test.app.service;

import com.alten.test.app.exception.ProductNotFoundException;
import com.alten.test.app.model.ProductDto;
import com.alten.test.app.repository.ProductRepository;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.service.mapper.ProductMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAll() {
        return productRepository.findAll().stream().map(productMapper::mapToProductDto).toList();
    }

    public ProductDto post(ProductDto newProductDto) {
       return productMapper.mapToProductDto(productRepository.save(productMapper.mapToProduct(newProductDto)));
    }

    public ProductDto getProductDto(Long id) {
        return productRepository.findById(id).map(productMapper::mapToProductDto).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductDto patch(Long id, ProductDto patchProductDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        Product patchProduct = productMapper.mapToProduct(patchProductDto);
        if (!ObjectUtils.isEmpty(patchProduct.getCode())) product.setCode(patchProduct.getCode());
        if (!ObjectUtils.isEmpty(patchProduct.getName())) product.setName(patchProduct.getName());
        if (!ObjectUtils.isEmpty(patchProduct.getDescription())) product.setDescription(patchProduct.getDescription());
        if (!ObjectUtils.isEmpty(patchProduct.getImage())) product.setImage(patchProduct.getImage());
        if (!ObjectUtils.isEmpty(patchProduct.getCategory())) product.setCategory(patchProduct.getCategory());
        if (patchProduct.getPrice() != null) product.setPrice(patchProduct.getPrice());
        if (patchProduct.getQuantity() != null) product.setQuantity(patchProduct.getQuantity());
        if (!ObjectUtils.isEmpty(patchProduct.getInternalReference()))
            product.setInternalReference(patchProduct.getInternalReference());
        if (patchProduct.getShellId() != null) product.setShellId(patchProduct.getShellId());
        if (patchProduct.getInventoryStatusEnum() != null)
            product.setInventoryStatusEnum(patchProduct.getInventoryStatusEnum());
        if (patchProduct.getRating() != null) product.setRating(patchProduct.getRating());
        return productMapper.mapToProductDto(productRepository.save(product));
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    public Product get(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }
}
