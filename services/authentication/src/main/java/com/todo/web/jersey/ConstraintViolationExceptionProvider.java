package com.todo.web.jersey;

import com.todo.web.contract.ErrorResponseBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionProvider extends InvalidRequestExceptionProvider<ConstraintViolationException> {
    @Override
    public Object createEntity(ConstraintViolationException exception) {
        ErrorResponseBuilder errorBuilder = new ErrorResponseBuilder();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorBuilder.withErrorMessage(violation.getMessage());
        }
        return errorBuilder.build();
    }
}
