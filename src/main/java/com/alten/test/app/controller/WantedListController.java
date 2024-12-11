package com.alten.test.app.controller;

import com.alten.test.app.model.WantedListDto;
import com.alten.test.app.service.WantedListService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wanted-list")
public class WantedListController {

    private final WantedListService wantedListService;

    public WantedListController(WantedListService wantedListService) {
        this.wantedListService = wantedListService;
    }

    //N'étant pas spécialiste du frontend je ne sais pas quel contrat adopter,
    //j'ai supposé que le frontend gère la liste en entier ainsi que les quantités avec MAJ via post

    @GetMapping
    public WantedListDto get() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wantedListService.get(userDetails.getUsername());
    }

    @PostMapping
    public WantedListDto post(@RequestBody WantedListDto wantedListDto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return wantedListService.post(userDetails.getUsername(), wantedListDto);
    }
}
