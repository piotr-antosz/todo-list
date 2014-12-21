package com.todo.web.jersey;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.todo.web.contract.ErrorResponseBuilder;

import javax.ws.rs.ext.Provider;

@Provider
public class UnrecognizedPropertyExceptionProvider extends InvalidRequestExceptionProvider<UnrecognizedPropertyException> {
    @Override
    public Object createEntity(UnrecognizedPropertyException throwable) {
        return new ErrorResponseBuilder()
                .withErrorMessage("unrecognized field " + throwable.getPropertyName())
                .build();
    }
}
