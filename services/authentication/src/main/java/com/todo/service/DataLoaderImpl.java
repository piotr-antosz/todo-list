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
            userRepository.saveAndFlush(new User(login, email, "06D1D5A2B5109E19F6A4EE42EB3B2A311E7CC584236B70B9638E30C4DDC38B3C"));
            LOG.info("Test account '{}' created. Log in with password '{}'", login, "abc123");
        } else {
            LOG.info("Adding test account '{}'/'{}' skipped. Account with that login/email already exists", login, email);
        }
    }
}
