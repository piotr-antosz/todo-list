package com.todo.web.endpoint;

import com.todo.service.UserService;
import com.todo.web.contract.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Component
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @PUT
    public void createUser(@NotNull(message = "you have to specify new user data") @Valid LoginData loginData) {
        userService.createUser(loginData.getEmail(), loginData.getPassword());
    }
}
