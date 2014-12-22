package com.todo.repository;

import com.todo.repository.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByValue(String value);
}
