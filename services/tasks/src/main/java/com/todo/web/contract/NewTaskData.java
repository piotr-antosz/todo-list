package com.todo.web.contract;

import org.hibernate.validator.constraints.NotBlank;

public class NewTaskData {
    @NotBlank
    private String description;

    public NewTaskData() {
    }

    public NewTaskData(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
