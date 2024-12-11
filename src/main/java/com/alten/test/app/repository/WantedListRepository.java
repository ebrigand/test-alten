package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.WantedList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WantedListRepository extends JpaRepository<WantedList, Long> {

    Optional<WantedList> findByAccount(Account account);

}
