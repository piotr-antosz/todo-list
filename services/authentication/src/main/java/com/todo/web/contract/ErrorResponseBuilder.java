package com.todo.web.contract;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseBuilder {
    private List<ErrorItem> errors = new ArrayList<>();

    public ErrorResponseBuilder withErrors(List<ErrorItem> errors) {
        this.errors.addAll(errors);
        return this;
    }

    public ErrorResponseBuilder withError(ErrorItem error) {
        this.errors.add(error);
        return this;
    }

    public ErrorResponseBuilder withErrorMessage(String message) {
        this.errors.add(new ErrorItem(message));
        return this;
    }

    public ErrorResponse build() {
        return new ErrorResponse(errors);
    }
}