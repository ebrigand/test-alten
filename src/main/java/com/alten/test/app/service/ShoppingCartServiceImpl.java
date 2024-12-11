package com.alten.test.app.service;

import com.alten.test.app.exception.AccountNotFoundException;
import com.alten.test.app.model.ShoppingCartDto;
import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.ShoppingCartRepository;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.ShoppingCart;
import com.alten.test.app.service.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final AccountRepository accountRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartMapper shoppingCartMapper,
                                   AccountRepository accountRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartMapper = shoppingCartMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public ShoppingCartDto post(String username, ShoppingCartDto shoppingCartDto) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        ShoppingCart shoppingCart = shoppingCartMapper.mapToShoppingCart(shoppingCartDto);
        shoppingCart.setAccount(account);
        ShoppingCart shoppingCartToSave = shoppingCartRepository.findByAccount(account).map(existingShoppingCart -> {
            existingShoppingCart.setProducts(shoppingCart.getProducts());
            return existingShoppingCart;
        }).orElse(shoppingCart);
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCartRepository.save(shoppingCartToSave));
    }

    @Override
    public ShoppingCartDto get(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        ShoppingCart shoppingCart = shoppingCartRepository.findByAccount(account).orElse(new ShoppingCart());
        return shoppingCartMapper.mapToShoppingCartDto(shoppingCart);
    }

}
