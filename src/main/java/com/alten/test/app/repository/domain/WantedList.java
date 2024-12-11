package com.alten.test.app.repository.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class WantedList {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "WANTED_LIST_ID")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", unique = true, nullable = false)
    private Account account;

    @OneToMany(mappedBy = "wantedList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductCount> productCounts = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WantedList that = (WantedList) o;
        return Objects.equals(id, that.id) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account);
    }
}
