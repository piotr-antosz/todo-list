package com.todo.service;

public interface AuthenticationService {
    String createToken(String login, String password);

    String getUid(String token);
}
