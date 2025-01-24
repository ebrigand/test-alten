package com.alten.test.app.integration;

import com.alten.test.app.enumeration.InventoryStatusEnum;
import com.alten.test.app.model.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ShoppingCartTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addOneShoppingCartAndGetIt() throws Exception {
        HttpHeaders headers = addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders();

        //Test save shopping cart, verify response
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(Lists.list(1L, 2L));
        HttpEntity<ShoppingCartDto> request6 = new HttpEntity<>(shoppingCartDto, headers);
        ShoppingCartDto shoppingCartDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/shopping-cart", request6, ShoppingCartDto.class).getBody();
        assert shoppingCartDtoResponse != null;
        assertThat(shoppingCartDtoResponse.productIds()).containsExactlyInAnyOrderElementsOf(shoppingCartDto.productIds());

        //get shopping cart, verify response contains the previous saved shopping cart
        HttpEntity<Void> request7 = new HttpEntity<>(null, headers);
        ShoppingCartDto shoppingCartDto2Response = this.restTemplate.exchange("http://localhost:" + port + "/shopping-cart", HttpMethod.GET, request7, ShoppingCartDto.class).getBody();
        assert shoppingCartDto2Response != null;
        assertThat(shoppingCartDto2Response.productIds()).containsExactlyInAnyOrderElementsOf(shoppingCartDto.productIds());

    }

    @Test
    void addOneShoppingCartAndReplaceIt() throws Exception {
        HttpHeaders headers = addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders();

        //Test save shopping cart, verify response
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(Lists.list(1L, 2L));
        HttpEntity<ShoppingCartDto> request6 = new HttpEntity<>(shoppingCartDto, headers);
        ShoppingCartDto shoppingCartDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/shopping-cart", request6, ShoppingCartDto.class).getBody();
        assert shoppingCartDtoResponse != null;
        assertThat(shoppingCartDtoResponse.productIds()).containsExactlyInAnyOrderElementsOf(shoppingCartDto.productIds());

        //Test save new shopping cart, verify response
        ShoppingCartDto shoppingCartDto2 = new ShoppingCartDto(Lists.list(1L));
        HttpEntity<ShoppingCartDto> request7 = new HttpEntity<>(shoppingCartDto2, headers);
        ShoppingCartDto shoppingCartDto2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/shopping-cart", request7, ShoppingCartDto.class).getBody();
        assert shoppingCartDto2Response != null;
        assertThat(shoppingCartDto2Response.productIds()).containsExactlyInAnyOrderElementsOf(shoppingCartDto2.productIds());
    }

    private HttpHeaders addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders() {
        //Save account, verify response
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin", List.of(new RoleDto("ADMIN")));
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = this.restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        //Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1",
                1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        HttpEntity<ProductDto> request3 = new HttpEntity<>(productDto1, headers);
        ProductDto productDto1Response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, ProductDto.class).getBody();
        assert productDto1Response != null;

        //Save product 2, verify response
        ProductDto productDto2 = new ProductDto(null, "code2", "name2", "description2", "image2", "category2",
                1L, 1L, "internal2", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        HttpEntity<ProductDto> request4 = new HttpEntity<>(productDto2, headers);
        ProductDto productDto2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request4, ProductDto.class).getBody();
        assert productDto2Response != null;

        //Test GET products, verify response
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ResponseEntity<ProductDto[]> productDtosResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, request5, ProductDto[].class);
        assert productDtosResponseEntity.getBody() != null;

        return headers;
    }
}
