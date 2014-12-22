package com.todo.repository.model;

import org.joda.time.DateTimeUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "taskId"}))
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer taskId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long creation;

    @Column
    private Long completion;

    private Task() {
    }

    public Task(Integer taskId, String userId, String description) {
        this.taskId = taskId;
        this.userId = userId;
        this.description = description;
        this.creation = DateTimeUtils.currentTimeMillis();
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Long getCreation() {
        return creation;
    }

    public Long getCompletion() {
        return completion;
    }

    public void setCompletion(Long completion) {
        this.completion = completion;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completion != null;
    }
}
