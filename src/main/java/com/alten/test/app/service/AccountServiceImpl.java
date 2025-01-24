package com.alten.test.app.service;

import com.alten.test.app.exception.AccountAlreadyExistsException;
import com.alten.test.app.exception.AccountNotFoundException;
import com.alten.test.app.model.AccountDto;
import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.RoleRepository;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.service.mapper.AccountMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository,
                              RoleRepository roleRepository,
                              AccountMapper accountMapper,
                              PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountDto post(AccountDto newAccountDto){
        Account account = accountMapper.mapToAccount(newAccountDto);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        try {
            //roleRepository.saveAll(account.getRoles());
            return accountMapper.mapToAccountDto(accountRepository.save(account));
        } catch (DataIntegrityViolationException e){
            throw new AccountAlreadyExistsException(newAccountDto.username(), newAccountDto.email());
        } catch (RuntimeException e){
            throw new RuntimeException(newAccountDto.username());
        }
    }

    @Override
    public AccountDto getByEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("Could not find account with email " + email));
        return accountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto getByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        return accountMapper.mapToAccountDto(account);
    }

}
