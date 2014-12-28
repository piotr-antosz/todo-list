package com.todo.web.contract;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class NewUserData extends LoginData {
    @NotBlank(message = "email may not be blank")
    @Email(message = "incorrect email format")
    private String email;

    public NewUserData() {
    }

    public NewUserData(String login, String email, String password) {
        super(login, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
