package com.todo.service;

import com.todo.repository.TaskRepository;
import com.todo.repository.model.Task;
import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.UpdateTaskData;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskData taskData;
    @Mock
    private NewTaskData newTaskData;
    @Mock
    private UpdateTaskData updateTaskData;
    @Mock
    private Task task;

    private final String userId = "userId";
    private final int taskId = 33;

    @InjectMocks
    private final TaskServiceImpl service = new TaskServiceImpl();

    @Test
    public void shouldCreateNewTask() {
        //given
        long creationDate = DateTimeUtils.currentTimeMillis();
        String description = "description";
        given(newTaskData.getDescription()).willReturn(description);
        given(taskRepository.findMaxTaskIdForUserId(userId)).willReturn(0);
        given(taskRepository.saveAndFlush(any(Task.class))).willReturn(task);
        given(task.getTaskId()).willReturn(1);
        given(task.getDescription()).willReturn(description);
        given(task.getCreation()).willReturn(creationDate);

        //when
        TaskData result = service.createTask(userId, newTaskData);

        //then
        assertThat(result, not(nullValue()));
        assertThat(result.getId(), equalTo(1));
        assertThat(result.getDescription(), equalTo(description));
        assertThat(result.getCreationDate(), not(nullValue()));
        assertThat(result.getCreationDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        assertThat(result.getCompletionDate(), nullValue());
        verify(taskRepository).findMaxTaskIdForUserId(userId);
        verify(taskRepository).saveAndFlush(any(Task.class));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotUpdateTaskWhenItNotExist() {
        //given
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(null);

        //when
        service.updateTask(userId, taskId, updateTaskData);

        //then
        fail("Exception should be thrown");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotGetTaskWhenItNotExist() {
        //given
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(null);

        //when
        service.getTask(userId, taskId);

        //then
        fail("Exception should be thrown");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldNotDeleteTaskWhenItNotExist() {
        //given
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(null);

        //when
        service.deleteTask(userId, taskId);

        //then
        fail("Exception should be thrown");
    }

    @Test
    public void shouldUpdateTask() {
        //given
        String description = "description";
        Task task = new Task(taskId, userId, "original");
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(task);
        given(updateTaskData.getDescription()).willReturn(description);
        given(updateTaskData.getCompleted()).willReturn(true);
        given(taskRepository.saveAndFlush(task)).willReturn(task);

        //when
        TaskData result = service.updateTask(userId, taskId, updateTaskData);

        //then
        assertThat(result, not(nullValue()));
        assertThat(result.getId(), equalTo(taskId));
        assertThat(result.getDescription(), equalTo(description));
        assertThat(result.getCreationDate(), not(nullValue()));
        assertThat(result.getCreationDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        assertThat(result.getCompletionDate(), not(nullValue()));
        assertThat(result.getCompletionDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        verify(taskRepository).findByUserIdAndTaskId(userId, taskId);
        verify(taskRepository).saveAndFlush(task);
    }

    @Test
    public void shouldGetTask() {
        //given
        String description = "description";
        Task task = new Task(taskId, userId, description);
        task.setCompletion(DateTimeUtils.currentTimeMillis());
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(task);

        //when
        TaskData result = service.getTask(userId, taskId);

        //then
        assertThat(result, not(nullValue()));
        assertThat(result.getId(), equalTo(taskId));
        assertThat(result.getDescription(), equalTo(description));
        assertThat(result.getCreationDate(), not(nullValue()));
        assertThat(result.getCreationDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        assertThat(result.getCompletionDate(), not(nullValue()));
        assertThat(result.getCompletionDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        verify(taskRepository).findByUserIdAndTaskId(userId, taskId);
    }

    @Test
    public void shouldGetAllTasks() {
        //given
        String description = "description";
        Task task = new Task(taskId, userId, description);
        task.setCompletion(DateTimeUtils.currentTimeMillis());
        given(taskRepository.findAllByUserId(userId)).willReturn(Arrays.asList(task));

        //when
        List<TaskData> result = service.getTasks(userId);

        //then
        assertThat(result, not(nullValue()));
        assertThat(result, hasSize(1));
        TaskData taskData = result.get(0);
        assertThat(taskData.getId(), equalTo(taskId));
        assertThat(taskData.getDescription(), equalTo(description));
        assertThat(taskData.getCreationDate(), not(nullValue()));
        assertThat(taskData.getCreationDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        assertThat(taskData.getCompletionDate(), not(nullValue()));
        assertThat(taskData.getCompletionDate().getMillis(), lessThanOrEqualTo(DateTimeUtils.currentTimeMillis()));
        verify(taskRepository).findAllByUserId(userId);
    }

    @Test
    public void shouldDeleteTask() {
        //given
        given(taskRepository.findByUserIdAndTaskId(userId, taskId)).willReturn(task);

        //when
        service.deleteTask(userId, taskId);

        //then
        verify(taskRepository).findByUserIdAndTaskId(userId, taskId);
        verify(taskRepository).delete(task);
    }
}