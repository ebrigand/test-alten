package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByAccount(Account account);

}
