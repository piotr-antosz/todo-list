package com.todo.web.jersey;

import com.todo.service.ResourceNotFoundException;
import com.todo.web.contract.ErrorResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionProvider implements ExceptionMapper<ResourceNotFoundException> {
    private final static Logger LOG = LoggerFactory.getLogger(ResourceNotFoundExceptionProvider.class);

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        LOG.info("Resource not found, returning 404", exception);
        return Response
                .status(404)
                .entity(new ErrorResponseBuilder()
                        .withErrorMessage(exception.getMessage())
                        .build())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
