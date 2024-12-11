package com.alten.test.app.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SHOPPING_CART_ID")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", unique = true, nullable = false)
    private Account account;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "SHOPPING_CART_PRODUCT",
            joinColumns = @JoinColumn(name = "SHOPPING_CART_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private Set<Product> products = new HashSet<>();
}
