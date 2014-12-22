package com.todo.service;

import com.todo.repository.TaskRepository;
import com.todo.repository.model.Task;
import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.TaskDataBuilder;
import com.todo.web.contract.UpdateTaskData;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskServiceImpl implements TaskService {
    private final static Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    @Override
    public TaskData createTask(String userId, NewTaskData newTaskData) {
        int maxTaskId = taskRepository.findMaxTaskIdForUserId(userId);
        Task task = new Task(maxTaskId + 1, userId, newTaskData.getDescription());
        task = taskRepository.saveAndFlush(task);
        LOG.info("Task {} created for user {}", task.getTaskId(), userId);
        return new TaskDataBuilder()
                .withTask(task)
                .build();
    }

    @Transactional
    @Override
    public TaskData updateTask(String userId, int taskId, UpdateTaskData updateTaskData) {
        Task task = getTaskForUser(userId, taskId);
        task.setDescription(updateTaskData.getDescription());
        if (Boolean.TRUE.equals(updateTaskData.getCompleted())) {
            task.setCompletion(DateTimeUtils.currentTimeMillis());
        } else {
            task.setCompletion(null);
        }
        task = taskRepository.saveAndFlush(task);
        LOG.info("Task {} updated for user {}", taskId, userId);
        return new TaskDataBuilder()
                .withTask(task)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TaskData getTask(String userId, int taskId) {
        Task task = getTaskForUser(userId, taskId);
        LOG.info("Task {} loaded for user {}", taskId, userId);
        return new TaskDataBuilder()
                .withTask(task)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskData> getTasks(String userId) {
        List<Task> allTasks = taskRepository.findAllByUserId(userId);
        LOG.info("All tasks loaded for user {}", userId);
        List<TaskData> result = new ArrayList<>(allTasks.size());
        for (Task task : allTasks) {
            result.add(new TaskDataBuilder()
                    .withTask(task)
                    .build());
        }
        return result;
    }

    @Transactional
    @Override
    public void deleteTask(String userId, int taskId) {
        Task task = getTaskForUser(userId, taskId);
        taskRepository.delete(task);
        LOG.info("Task {} deleted for user {}", taskId, userId);
    }

    private Task getTaskForUser(String userId, int taskId) {
        Task task = taskRepository.findByUserIdAndTaskId(userId, taskId);
        if (task == null) {
            throw new ResourceNotFoundException("task " + taskId + " doesn't exist");
        }
        return task;
    }
}
