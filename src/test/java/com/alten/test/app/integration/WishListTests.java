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

        //Test save wish list, verify response
        WishListDto wishListDto = WishListDto.builder().productCountDtos(
                Lists.list(
                        ProductCountDto.builder().productId(1L).build(),
                        ProductCountDto.builder().productId(2L).build())).build();
        HttpEntity<WishListDto> request6 = new HttpEntity<>(wishListDto, headers);
        WishListDto wishListDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request6, WishListDto.class).getBody();
        assert wishListDtoResponse != null;
        assertThat(wishListDtoResponse.getProductCountDtos().stream().map(
                ProductCountDto::getProductId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.getProductCountDtos().stream().map(
                        ProductCountDto::getProductId).toList());

        //get shopping cart, verify response contains the previous saved wish list
        HttpEntity<Void> request7 = new HttpEntity<>(null, headers);
        WishListDto wishListDto2Response = this.restTemplate.exchange("http://localhost:" + port + "/wish-list", HttpMethod.GET, request7, WishListDto.class).getBody();
        assert wishListDto2Response != null;
        assertThat(wishListDto2Response.getProductCountDtos().stream().map(
                ProductCountDto::getProductId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.getProductCountDtos().stream().map(
                        ProductCountDto::getProductId).toList());
    }

    @Test
    void addOneWishListAndReplaceIt() throws Exception {
        HttpHeaders headers = addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders();

        //Test save wish list, verify response
        WishListDto wishListDto = WishListDto.builder().productCountDtos(
                Lists.list(
                        ProductCountDto.builder().productId(1L).build(),
                        ProductCountDto.builder().productId(2L).build())).build();
        HttpEntity<WishListDto> request6 = new HttpEntity<>(wishListDto, headers);
        WishListDto wishListDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request6, WishListDto.class).getBody();
        assert wishListDtoResponse != null;
        assertThat(wishListDtoResponse.getProductCountDtos().stream().map(
                ProductCountDto::getProductId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto.getProductCountDtos().stream().map(
                        ProductCountDto::getProductId).toList());

        //Test save new wish list, verify response
        WishListDto wishListDto2 = WishListDto.builder().productCountDtos(
                Lists.list(
                        ProductCountDto.builder().productId(1L).build())).build();
        HttpEntity<WishListDto> request7 = new HttpEntity<>(wishListDto2, headers);
        WishListDto wishListDto2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/wish-list", request7, WishListDto.class).getBody();
        assert wishListDto2Response != null;
        assertThat(wishListDto2Response.getProductCountDtos().stream().map(
                ProductCountDto::getProductId).toList())
                .containsExactlyInAnyOrderElementsOf(wishListDto2.getProductCountDtos().stream().map(
                        ProductCountDto::getProductId).toList());
    }

    private HttpHeaders addAdminAccountAndGetTokenAndAddTwoProductsAndGetHeaders() {
        //Save account, verify response
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("admin").email("admin@admin.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("admin@admin.com").password("admin").build();
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
        ProductDto productDto1Response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request3, ProductDto.class).getBody();
        assert productDto1Response != null;

        //Save product 2, verify response
        ProductDto productDto2 = ProductDto.builder().image("image2").code("code2").name("name2")
                .code("code2").category("category2").description("description2").price(1L).internalReference("internal2")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
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
