package com.todo.web.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return objectMapper;
    }
}
