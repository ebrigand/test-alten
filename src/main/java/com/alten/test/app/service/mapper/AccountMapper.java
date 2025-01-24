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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    @Mapping(target = "roleDtos", source = "roles", qualifiedByName = "rolesToRoleDtos")
    AccountDto mapToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roleDtos", qualifiedByName = "roleDtosToRoles")
    Account mapToAccount(AccountDto accountDto);

    @Named("roleDtosToRoles")
    static Collection<Role> roleDtosToRoles(List<RoleDto> roleDtos) {
       return roleDtos.stream().map(roleDto -> Role.builder().name(roleDto.name()).build()).collect(Collectors.toList());
    }

    @Named("rolesToRoleDtos")
    static List<RoleDto> roleDtosToRoles(Collection<Role> roles) {
        return roles.stream().map(role -> new RoleDto(role.getName())).collect(Collectors.toList());
    }
}