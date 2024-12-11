package com.alten.test.app.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AccountDto implements Serializable {

    private String username;
    private String firstname;
    private String email;
    private String password;

}
