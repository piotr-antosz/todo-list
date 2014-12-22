package com.todo.web.contract;

public class UpdateTaskData extends NewTaskData {
    private Boolean completed;

    public UpdateTaskData() {
    }

    public UpdateTaskData(String description, Boolean completed) {
        super(description);
        this.completed = completed;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
