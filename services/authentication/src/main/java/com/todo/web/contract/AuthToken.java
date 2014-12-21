package com.todo.web.contract;

import org.hibernate.validator.constraints.NotBlank;

public class AuthToken {
    @NotBlank(message = "token cannot be blank")
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken() {
    }

    public String getToken() {
        return token;
    }
}
