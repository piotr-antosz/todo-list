package com.todo.service;

import com.todo.repository.UserRepository;
import com.todo.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoaderImpl implements DataLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DataLoaderImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void loadTestData() {
        String login = "base";
        String email = "test.email@domain.com";
        if (userRepository.findByLogin(login) == null && userRepository.findByEmail(email) == null) {
            userRepository.saveAndFlush(new User(login, email, "6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090"));
            LOG.info("Test account '{}' created. Log in with password '{}'", login, "abc123");
        } else {
            LOG.info("Adding test account '{}'/'{}' skipped. Account with that login/email already exists", login, email);
        }
    }
}
