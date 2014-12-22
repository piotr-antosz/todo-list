package com.todo.service;

import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.UpdateTaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskServiceImpl implements TaskService {
    private final static Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public TaskData createTask(String userId, NewTaskData newTaskData) {
        return null;
    }

    @Override
    public TaskData updateTask(String userId, int taskId, UpdateTaskData updateTaskData) {
        return null;
    }

    @Override
    public TaskData getTask(String userId, int taskId) {
        return null;
    }

    @Override
    public List<TaskData> getTasks(String userId) {
        return null;
    }

    @Override
    public void deleteTask(String userId, int taskId) {

    }
}
