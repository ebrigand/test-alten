package com.alten.test.app.security;

import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user with this user name"));

        return User.builder()
            .username(account.getUsername())
            .password(account.getPassword())
            .build();
    }
    
}