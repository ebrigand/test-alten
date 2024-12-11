package com.alten.test.app.controller;

import com.alten.test.app.model.ShoppingCartDto;
import com.alten.test.app.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    //N'étant pas spécialiste du frontend je ne sais pas quel contrat adopter,
    //j'ai supposé que le frontend gère la liste en entier avec MAJ via post

    @GetMapping
    public ShoppingCartDto get() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return shoppingCartService.get(userDetails.getUsername());
    }

    @PostMapping
    public ShoppingCartDto post(@RequestBody ShoppingCartDto shoppingCartDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return shoppingCartService.post(userDetails.getUsername(), shoppingCartDto);
    }
}
