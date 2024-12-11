package com.alten.test.app.repository.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class ProductCount {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", unique = true, nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "WANTED_LIST_ID", referencedColumnName = "WANTED_LIST_ID")
    private WantedList wantedList;

    private Integer quantity = 0;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductCount that = (ProductCount) o;
        return Objects.equals(id, that.id) && Objects.equals(product, that.product) && Objects.equals(wantedList, that.wantedList) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, wantedList, quantity);
    }
}
