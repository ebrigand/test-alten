package com.alten.test.app.service;

import com.alten.test.app.model.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto post(String username, ShoppingCartDto shoppingCartDto);
    ShoppingCartDto get(String username);
}
