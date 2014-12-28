package com.todo.service;

import com.todo.repository.TokenRepository;
import com.todo.repository.UserRepository;
import com.todo.repository.model.Token;
import com.todo.repository.model.User;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Value("${token.expire.inSeconds}")
    private int tokenExpireInSeconds;

    @Override
    public String createToken(String login, String password) {
        User user = userRepository.findByLoginAndPassword(login, password);
        if (user != null) {
            Token token = new Token(DateTimeUtils.currentTimeMillis(), user);
            LOG.info("creating token for user {}", user.getLogin());
            token = tokenRepository.saveAndFlush(token);
            return token.getValue();
        }
        throw new BusinessException("wrong login data");
    }

    @Transactional
    @Override
    public String getUid(String tokenValue) {
        Token token = tokenRepository.findByValue(tokenValue);
        if (token == null) {
            throw new BusinessException("incorrect token");
        }
        LocalDateTime lastUsage = new LocalDateTime(token.getLastUsage());
        if (lastUsage.plusSeconds(tokenExpireInSeconds).isAfter(LocalDateTime.now())) {
            token.setLastUsage(DateTimeUtils.currentTimeMillis());
            User user = token.getUser();
            LOG.info("updating token last usage for user {}", user.getLogin());
            tokenRepository.saveAndFlush(token);
            return user.getUserId();
        } else {
            tokenRepository.delete(token);
            throw new BusinessException("token expired");
        }
    }
}
