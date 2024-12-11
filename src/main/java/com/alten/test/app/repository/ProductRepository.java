package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
