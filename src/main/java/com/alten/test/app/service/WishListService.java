package com.alten.test.app.service;

import com.alten.test.app.model.WishListDto;

public interface WishListService {

    WishListDto post(String username, WishListDto wishListDto);
    WishListDto get(String username);
}
