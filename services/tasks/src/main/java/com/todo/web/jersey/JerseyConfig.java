package com.todo.web.jersey;

import com.todo.web.endpoint.TasksEndpoint;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        packages(getClass().getPackage().getName());

        //enable Jackson for JSON
        register(JacksonFeature.class);

        register(TasksEndpoint.class);
    }
}