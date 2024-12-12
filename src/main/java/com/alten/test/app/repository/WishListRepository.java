package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByAccount(Account account);

}
