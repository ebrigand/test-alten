package com.alten.test.app.unit;

import com.alten.test.app.controller.AccountController;
import com.alten.test.app.model.AccountDto;
import com.alten.test.app.model.RoleDto;
import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.Role;
import com.alten.test.app.service.AccountService;
import com.alten.test.app.service.AccountServiceImpl;
import com.alten.test.app.service.mapper.AccountMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountMapper accountMapper;

    @Test
    public void testGetByEmail() {
        // Given
        Account account = Account.builder().username("bob").password("bob").firstname("bob")
                .email("bob@gmail.com").roles(List.of(Role.builder().name("ADMIN").build())).build();
        when(accountRepository.findByEmail(any(String.class))).thenReturn(Optional.of(account));

        AccountDto accountDtoExpected = new AccountDto("bob", "bob", "bob@gmail.com", "bob", List.of(new RoleDto("ADMIN")));
        when(accountMapper.mapToAccountDto(account)).thenReturn(accountDtoExpected);

        AccountDto accountDtoActual = service.getByEmail("bob@gmail.com");

        assertEquals(accountDtoExpected, accountDtoActual);
    }
}
