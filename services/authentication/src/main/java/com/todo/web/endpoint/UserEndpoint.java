package com.todo.web.endpoint;

import com.todo.service.AuthenticationService;
import com.todo.service.UserService;
import com.todo.web.contract.AuthToken;
import com.todo.web.contract.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Component
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @POST
    public AuthToken createUser(@NotNull(message = "you have to specify new user data") @Valid LoginData loginData) {
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        userService.createUser(email, password);
        String token = authenticationService.createToken(email, password);
        return new AuthToken(token);
    }
}
