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
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        //Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1", 1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto1, headers);

        //Save product 2, verify response
        ProductDto productDto2 = new ProductDto(null, "code2", "name2", "description2", "image2", "category2", 1L, 1L, "internal2", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto2, headers);
    }

    @Test
    void deleteOneProduct() throws Exception {
        //Save account, verify response
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        //Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
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
        assertThat(productDtosResponseEntity.getBody()[0].id()).isEqualTo(2L);
    }

    @Test
    void getProducts() throws Exception {
        // Save account, verify response
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        // Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1", 1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto1, headers);

        // Save product 2, verify response
        ProductDto productDto2 = new ProductDto(null, "code2", "name2", "description2", "image2", "category2", 1L, 1L, "internal2", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto2, headers);

        // Test GET products, verify length = 2
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ResponseEntity<ProductDto[]> productDtosResponseEntity = this.restTemplate.exchange("http://localhost:" + port + "/products", HttpMethod.GET, request5, ProductDto[].class);
        assert productDtosResponseEntity.getBody() != null;
        assertThat(productDtosResponseEntity.getBody().length).isEqualTo(2);
    }

    @Test
    void getProduct() throws Exception {
        // Save account, verify response
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        // Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1", 1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto1, headers);

        // Test GET product, verify id = 1
        HttpEntity<Void> request4 = new HttpEntity<>(null, headers);
        ProductDto productDtosResponseEntityBody = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.GET, request4, ProductDto.class).getBody();
        assert productDtosResponseEntityBody != null;
        assertThat(productDtosResponseEntityBody.id()).isEqualTo(1L);
    }

    @Test
    void patchProduct() throws Exception {
        // Save account, verify response
        AccountDto accountDto = new AccountDto("username", "firstname", "admin@admin.com", "admin");
        HttpEntity<AccountDto> request1 = new HttpEntity<>(accountDto);
        AccountDto accountDtoResponse = restTemplate.postForEntity("http://localhost:" + port + "/account", request1, AccountDto.class).getBody();
        assert accountDtoResponse != null;

        // Get token
        LoginRequestDto loginRequestDto = new LoginRequestDto("admin@admin.com", "admin");
        HttpEntity<LoginRequestDto> request2 = new HttpEntity<>(loginRequestDto);
        String token = restTemplate.postForEntity("http://localhost:" + port + "/token", request2, String.class).getBody();
        assert token != null;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Save product 1, verify response
        ProductDto productDto1 = new ProductDto(null, "code1", "name1", "description1", "image1", "category1", 1L, 1L, "internal1", 1L, InventoryStatusEnum.LOWSTOCK, 1L, null, null);
        postAndVerifyProduct(productDto1, headers);

        // Patch product 1, verify response
        ProductDto productDto1Patch = new ProductDto(null, "newCode1", null, null, null, null, null, 2L, null, null, null, null, null, null);
        HttpEntity<ProductDto> request4 = new HttpEntity<>(productDto1Patch, headers);
        ProductDto patchProductDto1ResponseBody = this.restTemplate.patchForObject("http://localhost:" + port + "/products/1", request4, ProductDto.class);
        assert patchProductDto1ResponseBody != null;

        // Patched fields
        assertThat(patchProductDto1ResponseBody.code()).isEqualTo(productDto1Patch.code());
        assertThat(patchProductDto1ResponseBody.quantity()).isEqualTo(productDto1Patch.quantity());
        // Not patched fields
        assertThat(patchProductDto1ResponseBody.image()).isEqualTo(productDto1.image());
        assertThat(patchProductDto1ResponseBody.category()).isEqualTo(productDto1.category());
        assertThat(patchProductDto1ResponseBody.name()).isEqualTo(productDto1.name());
        assertThat(patchProductDto1ResponseBody.description()).isEqualTo(productDto1.description());
        assertThat(patchProductDto1ResponseBody.internalReference()).isEqualTo(productDto1.internalReference());
        assertThat(patchProductDto1ResponseBody.inventoryStatusEnum()).isEqualTo(productDto1.inventoryStatusEnum());
        assertThat(patchProductDto1ResponseBody.price()).isEqualTo(productDto1.price());
        assertThat(patchProductDto1ResponseBody.rating()).isEqualTo(productDto1.rating());
        assertThat(patchProductDto1ResponseBody.shellId()).isEqualTo(productDto1.shellId());

        // Test GET product 1, verify patched and not patched fields
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ProductDto getProductDto1ResponseBody = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.GET, request5, ProductDto.class).getBody();
        assert getProductDto1ResponseBody != null;
        // Patched fields
        assertThat(getProductDto1ResponseBody.code()).isEqualTo(productDto1Patch.code());
        assertThat(getProductDto1ResponseBody.quantity()).isEqualTo(productDto1Patch.quantity());
        // Not patched fields
        assertThat(getProductDto1ResponseBody.image()).isEqualTo(productDto1.image());
        assertThat(getProductDto1ResponseBody.category()).isEqualTo(productDto1.category());
        assertThat(getProductDto1ResponseBody.name()).isEqualTo(productDto1.name());
        assertThat(getProductDto1ResponseBody.description()).isEqualTo(productDto1.description());
        assertThat(getProductDto1ResponseBody.internalReference()).isEqualTo(productDto1.internalReference());
        assertThat(getProductDto1ResponseBody.inventoryStatusEnum()).isEqualTo(productDto1.inventoryStatusEnum());
        assertThat(getProductDto1ResponseBody.price()).isEqualTo(productDto1.price());
        assertThat(getProductDto1ResponseBody.rating()).isEqualTo(productDto1.rating());
        assertThat(getProductDto1ResponseBody.shellId()).isEqualTo(productDto1.shellId());
    }

    private void postAndVerifyProduct(ProductDto productDto, HttpHeaders headers) {
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, headers);
        ResponseEntity<ProductDto> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request, ProductDto.class);
        assert responseEntity != null && responseEntity.getBody() != null;
        ProductDto getProductDtoResponseBody = responseEntity.getBody();
        assertThat(getProductDtoResponseBody).isNotNull();
        assertThat(getProductDtoResponseBody.code()).isEqualTo(productDto.code());
        assertThat(getProductDtoResponseBody.quantity()).isEqualTo(productDto.quantity());
        assertThat(getProductDtoResponseBody.image()).isEqualTo(productDto.image());
        assertThat(getProductDtoResponseBody.category()).isEqualTo(productDto.category());
        assertThat(getProductDtoResponseBody.name()).isEqualTo(productDto.name());
        assertThat(getProductDtoResponseBody.description()).isEqualTo(productDto.description());
        assertThat(getProductDtoResponseBody.internalReference()).isEqualTo(productDto.internalReference());
        assertThat(getProductDtoResponseBody.inventoryStatusEnum()).isEqualTo(productDto.inventoryStatusEnum());
        assertThat(getProductDtoResponseBody.price()).isEqualTo(productDto.price());
        assertThat(getProductDtoResponseBody.rating()).isEqualTo(productDto.rating());
        assertThat(getProductDtoResponseBody.shellId()).isEqualTo(productDto.shellId());
        assertThat(getProductDtoResponseBody.createdAt()).isNotNull();
        assertThat(getProductDtoResponseBody.updatedAt()).isNotNull();
    }
}
