package com.todo.service;

import com.todo.repository.TokenRepository;
import com.todo.repository.UserRepository;
import com.todo.repository.model.Token;
import com.todo.repository.model.User;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private User user;
    @Mock
    private Token token;

    @InjectMocks
    private final AuthenticationServiceImpl service = new AuthenticationServiceImpl();

    @Test
    public void shouldCreateTokenForCorrectLoginAndPassword() {
        //given
        String login = "login";
        String password = "password";
        String tokenValue = "token";
        given(userRepository.findByLogin(login)).willReturn(user);
        given(user.getPassword()).willReturn(password);
        given(tokenRepository.saveAndFlush(any(Token.class))).willReturn(token);
        given(token.getValue()).willReturn(tokenValue);

        //when
        String result = service.createToken(login, password);

        //then
        assertThat(result, equalTo(tokenValue));
        verify(userRepository).findByLogin(login);
        verify(tokenRepository).saveAndFlush(any(Token.class));
    }

    @Test
    public void shouldCreateTokenForPasswordIgnoringCase() {
        //given
        String login = "login";
        String password = "password";
        String passwordUpper = "PASSWORD";
        String tokenValue = "token";
        given(userRepository.findByLogin(login)).willReturn(user);
        given(user.getPassword()).willReturn(password);
        given(tokenRepository.saveAndFlush(any(Token.class))).willReturn(token);
        given(token.getValue()).willReturn(tokenValue);

        //when
        String result = service.createToken(login, passwordUpper);

        //then
        assertThat(result, equalTo(tokenValue));
        verify(userRepository).findByLogin(login);
        verify(tokenRepository).saveAndFlush(any(Token.class));
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateTokenForIncorrectLoginAndPassword() {
        //given
        String login = "login";
        String password = "password";
        given(userRepository.findByLogin(login)).willReturn(null);

        //when
        service.createToken(login, password);

        //then
        fail("Exception should be thrown");
    }

    @Test(expected = BusinessException.class)
    public void shouldNotReturnUidForIncorrectToken() {
        //given
        String token = "token";
        given(tokenRepository.findByValue(token)).willReturn(null);

        //when
        service.getUid(token);

        //then
        fail("Exception should be thrown");
    }

    @Test
    public void shouldNotReturnUidForExpiredToken() {
        //given
        String tokenValue = "tokenValue";
        given(tokenRepository.findByValue(tokenValue)).willReturn(token);
        given(token.getLastUsage()).willReturn(DateTime.now().minusDays(1).getMillis());

        //when
        try {
            service.getUid(tokenValue);
            fail("Exception should be thrown");
        } catch (Exception e) {
            //then
            verify(tokenRepository).findByValue(tokenValue);
            verify(tokenRepository).delete(token);
            verify(tokenRepository, times(0)).saveAndFlush(any(Token.class));
            assertThat(e, instanceOf(BusinessException.class));
        }
    }

    @Test
    public void shouldReturnUidForCorrectToken() {
        //given
        String tokenValue = "tokenValue";
        String uid = "uid";
        given(tokenRepository.findByValue(tokenValue)).willReturn(token);
        given(token.getLastUsage()).willReturn(DateTime.now().plusDays(1).getMillis());
        given(token.getUser()).willReturn(user);
        given(user.getUserId()).willReturn(uid);

        //when
        String result = service.getUid(tokenValue);

        //then
        assertThat(result, equalTo(uid));
        verify(tokenRepository).findByValue(tokenValue);
        verify(tokenRepository).saveAndFlush(token);
        verify(tokenRepository, times(0)).delete(any(Token.class));
    }
}