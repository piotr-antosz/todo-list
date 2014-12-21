package com.todo.web.contract;

public class ErrorItem {
    private String message;

    public ErrorItem(String message) {
        this.message = message;
    }

    public ErrorItem() {
    }

    public String getMessage() {
        return message;
    }
}
