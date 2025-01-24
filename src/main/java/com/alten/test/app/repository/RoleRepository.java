package com.alten.test.app.repository;

import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
