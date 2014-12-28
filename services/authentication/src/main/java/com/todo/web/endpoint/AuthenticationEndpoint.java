package com.todo.web.endpoint;

import com.todo.service.AuthenticationService;
import com.todo.web.contract.AuthToken;
import com.todo.web.contract.LoginData;
import com.todo.web.contract.UserId;
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
@Path("/authentication")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationEndpoint {
    @Autowired
    private AuthenticationService authenticationService;

    @POST
    @Path("/validateToken")
    public UserId validateToken(@NotNull(message = "you have to specify authentication token") @Valid AuthToken authToken) {
        String uid = authenticationService.getUid(authToken.getToken());
        return new UserId(uid);
    }

    @POST
    @Path("/login")
    public AuthToken login(@NotNull(message = "you have to specify login data") @Valid LoginData loginData) {
        String token = authenticationService.createToken(loginData.getLogin(), loginData.getPassword());
        return new AuthToken(token);
    }
}
