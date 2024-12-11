package com.alten.test.app.service;

import com.alten.test.app.model.AccountDto;

public interface AccountService {

    AccountDto post(AccountDto newAccountDto);
    AccountDto getByEmail(String email);
    AccountDto getByUsername(String username);

}
