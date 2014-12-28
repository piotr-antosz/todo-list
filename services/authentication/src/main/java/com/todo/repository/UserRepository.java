package com.todo.repository;

import com.todo.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByLoginAndPassword(String login, String password);

    User findByLogin(String login);

    User findByEmail(String email);
}
