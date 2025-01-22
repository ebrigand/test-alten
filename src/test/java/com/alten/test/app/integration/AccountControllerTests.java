package com.alten.test.app.integration;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import com.alten.test.app.model.AccountDto;
import com.alten.test.app.model.LoginRequestDto;
import com.alten.test.app.model.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addAdminAccountAndAddNoAdminAccount() throws Exception {
        AccountDto adminAccountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(adminAccountDto);
        AccountDto accountDtoResponseBody1 = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponseBody1 != null;
        assertThat(accountDtoResponseBody1.username()).isEqualTo(adminAccountDto.username());
        assertThat(accountDtoResponseBody1.email()).isEqualTo(adminAccountDto.email());
        assertThat(accountDtoResponseBody1.firstname()).isEqualTo(adminAccountDto.firstname());

        AccountDto noAdminAccountDto = new AccountDto("username2", "firstname2", "truc@truc.com", "truc");
        HttpEntity<AccountDto> request2 = new HttpEntity<>(noAdminAccountDto);
        AccountDto accountDtoResponseBody2 = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request2, AccountDto.class).getBody();
        assert accountDtoResponseBody2 != null;
        assertThat(accountDtoResponseBody2.username()).isEqualTo(noAdminAccountDto.username());
        assertThat(accountDtoResponseBody2.email()).isEqualTo(noAdminAccountDto.email());
        assertThat(accountDtoResponseBody2.firstname()).isEqualTo(noAdminAccountDto.firstname());
    }

    @Test
    void addNoAdminAccountAndGetTokenAndAddOneProduct() throws Exception {
        AccountDto accountDto = new AccountDto("username", "firstname", "truc@truc.com", "truc");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class);

        // Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("truc@truc.com", "truc");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        ResponseEntity<String> tokenResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class);
        String token = tokenResponse.getBody();
        assert token != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1",
                1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        HttpEntity<ProductDto> request3 = new HttpEntity<>(productDto1, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, String.class);
        assert response != null;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}
