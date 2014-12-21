package com.todo.web.contract;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class LoginData {

    @Email(message = "incorrect email format")
    private String email;

    @NotBlank(message = "password may not be blank")
    @Size(min = 8, message = "password has to contain min 8 signs")
    private String password;

    public LoginData() {
    }

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
