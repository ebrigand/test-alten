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
        postAndVerifyProduct(productDto1, headers);

        //Save product 2, verify response
        ProductDto productDto2 = ProductDto.builder().image("image2").code("code2").name("name2")
                .code("code2").category("category2").description("description2").price(1L).internalReference("internal2")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
        postAndVerifyProduct(productDto2, headers);
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
        postAndVerifyProduct(productDto1, headers);

        //Save product 2, verify response
        ProductDto productDto2 = ProductDto.builder().image("image2").code("code2").name("name2")
                .code("code2").category("category2").description("description2").price(1L).internalReference("internal2")
                .inventoryStatusEnum(InventoryStatusEnum.LOWSTOCK).rating(1L).quantity(1L).shellId(1L).build();
        postAndVerifyProduct(productDto2, headers);

        //Test GET products, verify length = 2
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
        postAndVerifyProduct(productDto1, headers);

        //Test GET product, verify id = 1
        HttpEntity<Void> request4 = new HttpEntity<>(null, headers);
        ProductDto productDtosResponseEntityBody = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.GET, request4, ProductDto.class).getBody();
        assert productDtosResponseEntityBody != null;
        assertThat(productDtosResponseEntityBody.getId()).isEqualTo(1L);
    }

    @Test
    void patchProduct() throws Exception {
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
        postAndVerifyProduct(productDto1, headers);

        //patch product 1, verify response
        ProductDto productDto1Patch = ProductDto.builder().quantity(2L).code("newCode1").build();
        HttpEntity<ProductDto> request4 = new HttpEntity<>(productDto1Patch, headers);
        ProductDto patchProductDto1ResponseBody = this.restTemplate.patchForObject("http://localhost:" + port + "/products/1", request4, ProductDto.class);
        assert patchProductDto1ResponseBody != null;
        //Patched fields
        assertThat(patchProductDto1ResponseBody.getCode()).isEqualTo(productDto1Patch.getCode());
        assertThat(patchProductDto1ResponseBody.getQuantity()).isEqualTo(productDto1Patch.getQuantity());
        //Not patched fields
        assertThat(patchProductDto1ResponseBody.getImage()).isEqualTo(productDto1.getImage());
        assertThat(patchProductDto1ResponseBody.getCategory()).isEqualTo(productDto1.getCategory());
        assertThat(patchProductDto1ResponseBody.getName()).isEqualTo(productDto1.getName());
        assertThat(patchProductDto1ResponseBody.getDescription()).isEqualTo(productDto1.getDescription());
        assertThat(patchProductDto1ResponseBody.getInternalReference()).isEqualTo(productDto1.getInternalReference());
        assertThat(patchProductDto1ResponseBody.getInventoryStatusEnum()).isEqualTo(productDto1.getInventoryStatusEnum());
        assertThat(patchProductDto1ResponseBody.getPrice()).isEqualTo(productDto1.getPrice());
        assertThat(patchProductDto1ResponseBody.getRating()).isEqualTo(productDto1.getRating());
        assertThat(patchProductDto1ResponseBody.getShellId()).isEqualTo(productDto1.getShellId());

        //Test GET product 1, verify patched and not patched fields
        HttpEntity<Void> request5 = new HttpEntity<>(null, headers);
        ProductDto getProductDto1ResponseBody = this.restTemplate.exchange("http://localhost:" + port + "/products/1", HttpMethod.GET, request5, ProductDto.class).getBody();
        assert getProductDto1ResponseBody != null;
        //Patched fields
        assertThat(getProductDto1ResponseBody.getCode()).isEqualTo(productDto1Patch.getCode());
        assertThat(getProductDto1ResponseBody.getQuantity()).isEqualTo(productDto1Patch.getQuantity());
        //Not patched fields
        assertThat(getProductDto1ResponseBody.getImage()).isEqualTo(productDto1.getImage());
        assertThat(getProductDto1ResponseBody.getCategory()).isEqualTo(productDto1.getCategory());
        assertThat(getProductDto1ResponseBody.getName()).isEqualTo(productDto1.getName());
        assertThat(getProductDto1ResponseBody.getDescription()).isEqualTo(productDto1.getDescription());
        assertThat(getProductDto1ResponseBody.getInternalReference()).isEqualTo(productDto1.getInternalReference());
        assertThat(getProductDto1ResponseBody.getInventoryStatusEnum()).isEqualTo(productDto1.getInventoryStatusEnum());
        assertThat(getProductDto1ResponseBody.getPrice()).isEqualTo(productDto1.getPrice());
        assertThat(getProductDto1ResponseBody.getRating()).isEqualTo(productDto1.getRating());
        assertThat(getProductDto1ResponseBody.getShellId()).isEqualTo(productDto1.getShellId());
    }

    private void postAndVerifyProduct(ProductDto productDto, HttpHeaders headers) {
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto, headers);
        ProductDto getProductDtoResponseBody = this.restTemplate.postForEntity("http://localhost:" + port + "/products", request, ProductDto.class).getBody();
        assert getProductDtoResponseBody != null;
        assertThat(getProductDtoResponseBody.getCode()).isEqualTo(productDto.getCode());
        assertThat(getProductDtoResponseBody.getQuantity()).isEqualTo(productDto.getQuantity());
        assertThat(getProductDtoResponseBody.getImage()).isEqualTo(productDto.getImage());
        assertThat(getProductDtoResponseBody.getCategory()).isEqualTo(productDto.getCategory());
        assertThat(getProductDtoResponseBody.getName()).isEqualTo(productDto.getName());
        assertThat(getProductDtoResponseBody.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(getProductDtoResponseBody.getInternalReference()).isEqualTo(productDto.getInternalReference());
        assertThat(getProductDtoResponseBody.getInventoryStatusEnum()).isEqualTo(productDto.getInventoryStatusEnum());
        assertThat(getProductDtoResponseBody.getPrice()).isEqualTo(productDto.getPrice());
        assertThat(getProductDtoResponseBody.getRating()).isEqualTo(productDto.getRating());
        assertThat(getProductDtoResponseBody.getShellId()).isEqualTo(productDto.getShellId());
    }
}
