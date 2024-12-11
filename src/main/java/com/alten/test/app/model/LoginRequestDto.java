package com.alten.test.app.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginRequestDto implements Serializable {

    private String email;
    private String password;

}
