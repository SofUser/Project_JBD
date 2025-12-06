package com.jbd.project_jbd.repository;

import com.jbd.project_jbd.entity.Task;
import com.jbd.project_jbd.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndTaskGroupId(Long userId, Long groupId);

    @Query("SELECT t.status, COUNT(t) " +
             "FROM Task t " +
            "WHERE t.user.id = :userId " +
            "GROUP BY t.status")
    List<Object[]> getTaskStatusStatsByUser(@Param("userId") Long userId);

    @Query("SELECT tg.name, COUNT(t) " +
             "FROM TaskGroup tg " +
             "LEFT JOIN tg.tasks t " +
            "WHERE tg.user.id = :userId " +
            "GROUP BY tg.id, tg.name")
    List<Object[]> getTaskCountByGroupForUser(@Param("userId") Long userId);
}