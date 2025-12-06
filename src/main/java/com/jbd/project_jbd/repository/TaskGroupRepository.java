package com.jbd.project_jbd.repository;

import com.jbd.project_jbd.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findByUserId(Long userId);
    Optional<TaskGroup> findByIdAndUserId(Long id, Long userId);
}