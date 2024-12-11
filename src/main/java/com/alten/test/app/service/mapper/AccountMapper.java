package com.alten.test.app.service.mapper;

import com.alten.test.app.model.AccountDto;
import com.alten.test.app.repository.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto mapToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    Account mapToAccount(AccountDto accountDto);
}