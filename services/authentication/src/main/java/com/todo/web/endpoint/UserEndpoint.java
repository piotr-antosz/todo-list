package com.todo.web.endpoint;

import com.todo.service.AuthenticationService;
import com.todo.service.UserService;
import com.todo.web.contract.AuthToken;
import com.todo.web.contract.NewUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @POST
    public AuthToken createUser(@NotNull(message = "you have to specify new user data") @Valid NewUserData userData) {
        userService.createUser(userData);
        String token = authenticationService.createToken(userData.getLogin(), userData.getPassword());
        return new AuthToken(token);
    }
}
