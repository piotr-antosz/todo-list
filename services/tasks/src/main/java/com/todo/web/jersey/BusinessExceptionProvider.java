package com.todo.web.jersey;

import com.todo.service.BusinessException;
import com.todo.web.contract.ErrorResponseBuilder;

import javax.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionProvider extends InvalidRequestExceptionProvider<BusinessException> {
    @Override
    public Object createEntity(BusinessException throwable) {
        return new ErrorResponseBuilder()
                .withErrorMessage(throwable.getMessage())
                .build();
    }
}
