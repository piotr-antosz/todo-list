package com.todo.web.jersey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class InvalidRequestExceptionProvider<T extends Throwable> implements ExceptionMapper<T> {
    private final static Logger LOG = LoggerFactory.getLogger(InvalidRequestExceptionProvider.class);

    @Override
    public Response toResponse(T throwable) {
        LOG.info("Invalid request, returning 422", throwable);
        return Response
                .status(422)
                .entity(createEntity(throwable))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public abstract Object createEntity(T throwable);
}
