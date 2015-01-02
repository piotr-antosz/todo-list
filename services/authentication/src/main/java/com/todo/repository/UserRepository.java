package com.todo.repository;

import com.todo.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByLogin(String login);

    User findByEmail(String email);
}
