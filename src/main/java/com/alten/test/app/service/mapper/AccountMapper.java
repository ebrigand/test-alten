package com.alten.test.app.service.mapper;

import com.alten.test.app.model.AccountDto;
import com.alten.test.app.model.RoleDto;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.Product;
import com.alten.test.app.repository.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = RoleMapper.class)
public interface AccountMapper {

    @Mapping(target = "roleDtos", source = "roles")
    AccountDto mapToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roleDtos")
    Account mapToAccount(AccountDto accountDto);


}