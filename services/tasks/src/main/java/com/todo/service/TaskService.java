package com.todo.service;

import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.UpdateTaskData;

import java.util.List;

public interface TaskService {

    TaskData createTask(String userId, NewTaskData newTaskData);

    TaskData updateTask(String userId, int taskId, UpdateTaskData updateTaskData);

    TaskData getTask(String userId, int taskId);

    List<TaskData> getTasks(String userId);

    void deleteTask(String userId, int taskId);
}
