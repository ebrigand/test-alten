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
class WishListTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addOneWishListAndGetIt() throws Exception {
        HttpHeaders headers = addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders();

        // Test save wish list, verify response
        WishListDto wishListDto = new WishListDto(
                Lists.list(
                        new ProductCountDto(1L, null),
                        new ProductCountDto(2L, null)));
        HttpEntity<WishListDto> request6 = new HttpEntity<>(wishListDto, headers);
        ResponseEntity<WishListDto> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request6, WishListDto.class);
        WishListDto wishListDtoResponse = responseEntity.getBody();
        assert responseEntity.getStatusCode().is2xxSuccessful();
        assert wishListDtoResponse != null;
        assertThat(wishListDtoResponse.productCountDtos().stream().map(
                ProductCountDto::productId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.productCountDtos().stream().map(
                        ProductCountDto::productId).toList());

        // Get wish list, verify response contains the previously saved wish list
        ResponseEntity<WishListDto> getResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/wish-list", HttpMethod.GET, new HttpEntity<>(headers), WishListDto.class);
        WishListDto retrievedWishList = getResponseEntity.getBody();
        assert getResponseEntity.getStatusCode().is2xxSuccessful();
        assert retrievedWishList != null;
        assertThat(retrievedWishList.productCountDtos().stream().map(
                ProductCountDto::productId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.productCountDtos().stream().map(
                        ProductCountDto::productId).toList());
    }

    @Test
    void addOneWishListAndReplaceIt() throws Exception {
        HttpHeaders headers = addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders();

        // Test save wish list, verify response
        WishListDto wishListDto = new WishListDto(
                Lists.list(
                        new ProductCountDto(1L, null),
                        new ProductCountDto(2L, null)));
        HttpEntity<WishListDto> request6 = new HttpEntity<>(wishListDto, headers);
        WishListDto wishListDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request6, WishListDto.class).getBody();
        assert wishListDtoResponse != null;
        assertThat(wishListDtoResponse.productCountDtos().stream().map(
                ProductCountDto::productId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.productCountDtos().stream().map(
                        ProductCountDto::productId).toList());

        // Test save new wish list, verify response
        WishListDto wishListDto2 = new WishListDto(
                Lists.list(
                        new ProductCountDto(1L, null)));
        HttpEntity<WishListDto> request7 = new HttpEntity<>(wishListDto2, headers);
        WishListDto wishListDto2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request7, WishListDto.class).getBody();
        assert wishListDto2Response != null;
        assertThat(wishListDto2Response.productCountDtos().stream().map(
                ProductCountDto::productId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto2.productCountDtos().stream().map(
                        ProductCountDto::productId).toList());
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
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1", 1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        HttpEntity<ProductDto> request3 = new HttpEntity<>(productDto1, headers);
        ProductDto productDto1Response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, ProductDto.class).getBody();
        assert productDto1Response != null;

        //Save product 2, verify response
        ProductDto productDto2 = new ProductDto(null, "code2", "name2", "description2", "image2", "category2", 1L, 1L, "internal2", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
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
