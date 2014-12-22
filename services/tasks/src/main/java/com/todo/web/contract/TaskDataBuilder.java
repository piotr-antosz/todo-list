package com.todo.web.contract;

import com.todo.repository.model.Task;
import org.joda.time.DateTime;

public class TaskDataBuilder {
    private String description;
    private Integer id;
    private DateTime creationDate;
    private DateTime completionDate;

    public TaskDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskDataBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TaskDataBuilder withCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public TaskDataBuilder withCompletionDate(DateTime completionDate) {
        this.completionDate = completionDate;
        return this;
    }

    public TaskDataBuilder withTask(Task task) {
        withId(task.getTaskId());
        withDescription(task.getDescription());
        withCreationDate(new DateTime(task.getCreation()));
        if (task.isCompleted()) {
            withCompletionDate(new DateTime(task.getCompletion()));
        }
        return this;
    }

    public TaskData build() {
        return new TaskData(description, id, creationDate, completionDate);
    }
}