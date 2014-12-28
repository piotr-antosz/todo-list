package com.todo.service;

import com.todo.repository.UserRepository;
import com.todo.repository.model.User;
import com.todo.web.contract.NewUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceImpl implements UserService {
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(NewUserData userData) {
        String login = userData.getLogin();
        User user = userRepository.findByLogin(login);
        if (user != null) {
            throw new BusinessException("user with that login already exists");
        }
        String email = userData.getEmail();
        user = userRepository.findByEmail(email);
        if (user != null) {
            throw new BusinessException("user with that email already exists");
        }
        user = new User(login, email, userData.getPassword());
        LOG.info("creating user with login {}", login);
        userRepository.saveAndFlush(user);
    }
}
