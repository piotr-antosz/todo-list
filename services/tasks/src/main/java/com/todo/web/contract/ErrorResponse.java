package com.todo.web.contract;

import java.util.List;

public class ErrorResponse {
    private List<ErrorItem> errors;

    public ErrorResponse(List<ErrorItem> errors) {
        this.errors = errors;
    }

    public ErrorResponse() {
    }

    public List<ErrorItem> getErrors() {
        return errors;
    }
}
