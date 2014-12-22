package com.todo.repository;

import com.todo.repository.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select coalesce(max(t.taskId), 0) from Task t where t.userId = :userId")
    int findMaxTaskIdForUserId(@Param("userId") String userId);

    Task findByUserIdAndTaskId(String userId, Integer taskId);

    @Query("select t from Task t where t.userId = :userId")
    List<Task> findAllByUserId(@Param("userId") String userId);
}
