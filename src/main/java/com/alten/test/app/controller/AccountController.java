package com.alten.test.app.controller;

import com.alten.test.app.model.AccountDto;
import com.alten.test.app.model.LoginRequestDto;
import com.alten.test.app.security.JwtTokenUtil;
import com.alten.test.app.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AccountController(AccountService accountService, AuthenticationManager authenticationManager,
                             JwtTokenUtil jwtTokenUtil) {
        this.accountService = accountService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/account")
    public AccountDto post(@RequestBody AccountDto accountDto) {
        return accountService.post(accountDto);
    }

    @PostMapping("/token")
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        AccountDto accountDto = accountService.getByEmail(loginRequestDto.email());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDto.username(), loginRequestDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateJwtToken(authentication);
    }

}
