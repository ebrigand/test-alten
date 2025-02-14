package com.alten.test.app.controller;

import com.alten.test.app.exception.UnauthorizedException;
import com.alten.test.app.model.AccountDto;
import com.alten.test.app.model.ProductDto;
import com.alten.test.app.service.AccountService;
import com.alten.test.app.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Value("${account.admin.email}")
    private String adminAccountEmail;

    private final ProductService productService;

    private final AccountService accountService;

    public ProductController(ProductService productService,
                             AccountService accountService) {
        this.productService = productService;
        this.accountService = accountService;
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    @PostMapping
    public ProductDto post(@RequestBody ProductDto newProductDto) {
        return productService.post(newProductDto);
    }

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable Long id) {
        return productService.getProductDto(id);
    }

    @PatchMapping("/{id}")
    public ProductDto patch(@PathVariable Long id, @RequestBody ProductDto patchProductDto) {
        return productService.patch(id, patchProductDto);
     }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    //Peut être moyen de gérer celà dans un filtre ou dans la methode WebSecurityConfig.filterChain(HttpSecurity http)
    //Dans la vrai vie je poserais la question aux collègues ou sur stackoverflow
    private void checkAuthorized(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AccountDto accountDto = accountService.getByUsername(userDetails.getUsername());
        if(!accountDto.email().equals(adminAccountEmail)) {
            throw new UnauthorizedException(userDetails.getUsername());
        }
    }
}
