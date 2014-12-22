package com.todo.web.contract;

public class TaskData extends UpdateTaskData {
    private Integer id;
    private String creationDate;
    private String completionDate;

    public TaskData() {
    }

    public TaskData(String description, Boolean completed, Integer id, String creationDate, String completionDate) {
        super(description, completed);
        this.id = id;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
    }

    public Integer getId() {
        return id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }
}
