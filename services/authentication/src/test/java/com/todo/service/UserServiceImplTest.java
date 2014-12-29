package com.todo.service;

import com.todo.repository.UserRepository;
import com.todo.repository.model.User;
import com.todo.web.contract.NewUserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;
    @Mock
    private NewUserData newUserData;

    @InjectMocks
    private final UserServiceImpl service = new UserServiceImpl();

    @Test(expected = BusinessException.class)
    public void shouldNotCreateUserIfLoginExists() {
        //given
        String login = "login";
        given(newUserData.getLogin()).willReturn(login);
        given(userRepository.findByLogin(login)).willReturn(user);

        //when
        service.createUser(newUserData);

        //then
        fail("Exception should be thrown");
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateUserIfEmailExists() {
        //given
        String email = "email";
        given(newUserData.getEmail()).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(user);

        //when
        service.createUser(newUserData);

        //then
        fail("Exception should be thrown");
    }

    @Test
    public void shouldCreateUserWhenNewData() {
        //given
        final String login = "login";
        final String email = "email";
        given(newUserData.getLogin()).willReturn(login);
        given(userRepository.findByLogin(login)).willReturn(null);
        given(newUserData.getEmail()).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(null);

        //when
        service.createUser(newUserData);

        //then
        verify(userRepository).findByLogin(login);
        verify(userRepository).findByEmail(email);
        verify(userRepository).saveAndFlush(argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof User
                        && login.equals(((User) o).getLogin())
                        && email.equals(((User) o).getEmail());
            }
        }));
    }
}