package com.todo.service;

import com.todo.repository.UserRepository;
import com.todo.repository.model.User;
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
    public void createUser(String login, String password) {
        User user = userRepository.findByEmail(login);
        if (user != null) {
            throw new BusinessException("user already exists");
        }
        user = new User(login, password);
        LOG.info("creating user with email {}", login);
        userRepository.saveAndFlush(user);
    }
}
