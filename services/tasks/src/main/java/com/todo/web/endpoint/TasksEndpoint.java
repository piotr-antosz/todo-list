package com.todo.web.endpoint;

import com.todo.service.TaskService;
import com.todo.web.contract.NewTaskData;
import com.todo.web.contract.TaskData;
import com.todo.web.contract.UpdateTaskData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TasksEndpoint {
    @Autowired
    private TaskService taskService;

    @POST
    public TaskData addTask(
            @NotNull(message = "you have to specify new task data")
            @Valid NewTaskData newTaskData,
            @HeaderParam("userId") String userId) {
        return taskService.createTask(userId, newTaskData);
    }

    @PUT
    @Path("{taskId}")
    public TaskData updateTask(
            @NotNull(message = "you have to specify task data to update")
            @Valid UpdateTaskData updateTaskData,
            @PathParam("taskId") int taskId,
            @HeaderParam("userId") String userId) {
        return taskService.updateTask(userId, taskId, updateTaskData);
    }

    @GET
    @Path("{taskId}")
    public TaskData getTask(
            @PathParam("taskId") int taskId,
            @HeaderParam("userId") String userId) {
        return taskService.getTask(userId, taskId);
    }

    @GET
    public List<TaskData> getTasks(@HeaderParam("userId") String userId) {
        return taskService.getTasks(userId);
    }

    @DELETE
    @Path("{taskId}")
    public void addTask(
            @PathParam("taskId") int taskId,
            @HeaderParam("userId") String userId) {
        taskService.deleteTask(userId, taskId);
    }
}
