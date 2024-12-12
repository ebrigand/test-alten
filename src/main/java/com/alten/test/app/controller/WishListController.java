package com.alten.test.app.controller;

import com.alten.test.app.model.WishListDto;
import com.alten.test.app.service.WishListService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wish-list")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    //N'étant pas spécialiste du frontend je ne sais pas quel contrat adopter,
    //j'ai supposé que le frontend gère la liste en entier ainsi que les quantités avec MAJ via post

    @GetMapping
    public WishListDto get() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wishListService.get(userDetails.getUsername());
    }

    @PostMapping
    public WishListDto post(@RequestBody WishListDto wishListDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wishListService.post(userDetails.getUsername(), wishListDto);
    }
}
