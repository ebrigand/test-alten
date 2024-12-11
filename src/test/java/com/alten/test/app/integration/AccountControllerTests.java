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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addNoAdminAccountAndGetTokenAndAddOneProduct() throws Exception {
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("truc").email("truc@truc.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("truc@truc.com").password("truc").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = this.restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        //Save product 1, verify response
        ProductDto productDto1 = ProductDto.builder().image("image1").code("code1").name("name1")
                .code("code1").category("category1").description("description1").price(1L).internalReference("internal1")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
        HttpEntity<ProductDto> request3 = new HttpEntity<>(productDto1, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, String.class);
        assert response != null;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void addAccountWithSameEmailTwice() throws Exception {
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("truc").email("truc@truc.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();

        accountDto = AccountDto.builder().firstname("firstname2").username("username2").password("truc").email("truc@truc.com").build();
        HttpEntity<AccountDto> request2 = new HttpEntity<>(accountDto);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request2, String.class);

        assert response != null;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void addNoAdminAccountAndCallGetTokenWithIncorrectPassword() throws Exception {
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("truc").email("truc@truc.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("truc@truc.com").password("admin").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = this.restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        //Save product 1, verify response
        ProductDto productDto1 = ProductDto.builder().image("image1").code("code1").name("name1")
                .code("code1").category("category1").description("description1").price(1L).internalReference("internal1")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
        HttpEntity<ProductDto> request3 = new HttpEntity<>(productDto1, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, String.class);
        assert response != null;
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
