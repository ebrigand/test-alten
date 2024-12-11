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
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void addTwoProducts() throws Exception {
        //Save account, verify response
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("admin").email("admin@admin.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("admin@admin.com").password("admin").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
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
        assertThat(productDto1Response.getImage()).contains(productDto1.getImage());
        assertThat(productDto1Response.getCategory()).contains(productDto1.getCategory());
        assertThat(productDto1Response.getCode()).contains(productDto1.getCode());
        //Test all the other properties

        //Save product 2, verify response
        ProductDto productDto2 = ProductDto.builder().image("image2").code("code2").name("name2")
                .code("code2").category("category2").description("description2").price(1L).internalReference("internal2")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
        HttpEntity<ProductDto> request4 = new HttpEntity<>(productDto2, headers);
        ProductDto productDto2Response = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request4, ProductDto.class).getBody();
        assert productDto2Response != null;
        assertThat(productDto2Response.getImage()).contains(productDto2.getImage());
        assertThat(productDto2Response.getCategory()).contains(productDto2.getCategory());
        assertThat(productDto2Response.getCode()).contains(productDto2.getCode());
        //Test all the other properties
    }

    @Test
    void deleteOneProduct() throws Exception {
        //Save account, verify response
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("admin").email("admin@admin.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("admin@admin.com").password("admin").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
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

        //Test delete product with id = 1
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ResponseEntity<Void> deleteResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.DELETE, request5, Void.class);
        assert deleteResponseEntity != null;
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Test GET products, verify length = 1, verify id = 2
        HttpEntity<Void> request6 = new HttpEntity<>(null, headers);
        ResponseEntity<ProductDto[]> productDtosResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, request6, ProductDto[].class);
        assert productDtosResponseEntity.getBody() != null;
        assertThat(productDtosResponseEntity.getBody().length).isEqualTo(1);
        assertThat(Arrays.stream(productDtosResponseEntity.getBody()).map(ProductDto::getId).findFirst().get()).isEqualTo(2L);
    }

    @Test
    void getProducts() throws Exception {
        //Save account, verify response
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("admin").email("admin@admin.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("admin@admin.com").password("admin").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
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

        //Test all the other properties= 2
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ResponseEntity<ProductDto[]> productDtosResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, request5, ProductDto[].class);
        assert productDtosResponseEntity.getBody() != null;
        assertThat(productDtosResponseEntity.getBody().length).isEqualTo(2);
    }

    @Test
    void getProduct() throws Exception {
        //Save account, verify response
        AccountDto accountDto = AccountDto.builder().firstname("firstname").username("username").password("admin").email("admin@admin.com").build();
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().email("admin@admin.com").password("admin").build();
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();

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

        //Test GET product, verify id = 1
        HttpEntity<Void> request4 = new HttpEntity<>(null, headers);
        ProductDto productDtosResponseEntityBody = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.GET, request4, ProductDto.class).getBody();
        assert productDtosResponseEntityBody != null;
        assertThat(productDtosResponseEntityBody.getId()).isEqualTo(1L);
    }
}
