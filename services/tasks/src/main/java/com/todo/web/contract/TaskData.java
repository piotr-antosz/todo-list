package com.todo.web.contract;

import org.joda.time.DateTime;

public class TaskData extends NewTaskData {
    private Integer id;
    private DateTime creationDate;
    private DateTime completionDate;

    public TaskData() {
    }

    TaskData(String description, Integer id, DateTime creationDate, DateTime completionDate) {
        super(description);
        this.id = id;
        this.creationDate = creationDate;
        this.completionDate = completionDate;
    }

    public Integer getId() {
        return id;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public DateTime getCompletionDate() {
        return completionDate;
    }
}
