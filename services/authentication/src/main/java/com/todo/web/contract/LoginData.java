package com.todo.web.contract;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class LoginData {

    @NotBlank(message = "login may not be blank")
    @Size(min = 4, message = "login has to contain min 8 signs")
    private String login;

    @NotBlank(message = "password may not be blank")
    @Size(min = 8, message = "password has to contain min 8 signs")
    private String password;

    public LoginData() {
    }

    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
