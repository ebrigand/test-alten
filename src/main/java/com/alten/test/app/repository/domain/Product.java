package com.alten.test.app.repository.domain;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Long price;
    private Long quantity;
    private String internalReference;
    private Long shellId;
    private InventoryStatusEnum inventoryStatusEnum;
    private Long rating;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(image, that.image) && Objects.equals(category, that.category) && Objects.equals(price, that.price) && Objects.equals(quantity, that.quantity) && Objects.equals(internalReference, that.internalReference) && Objects.equals(shellId, that.shellId) && inventoryStatusEnum == that.inventoryStatusEnum && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, description, image, category, price, quantity, internalReference, shellId, inventoryStatusEnum, rating);
    }
}
