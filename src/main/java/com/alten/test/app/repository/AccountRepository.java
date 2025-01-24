package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(@NonNull String email);

    Optional<Account> findByUsername(@NonNull String username);

}
