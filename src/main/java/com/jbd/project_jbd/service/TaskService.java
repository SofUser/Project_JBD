package com.jbd.project_jbd.service;

import com.jbd.project_jbd.entity.Task;
import com.jbd.project_jbd.entity.TaskGroup;
import com.jbd.project_jbd.entity.TaskStatus;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.repository.TaskRepository;
import com.jbd.project_jbd.repository.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskGroupRepository taskGroupRepository;

    public TaskService(TaskRepository taskRepository, TaskGroupRepository taskGroupRepository) {
        this.taskRepository = taskRepository;
        this.taskGroupRepository = taskGroupRepository;
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task, User user) {
        task.setUser(user);

        if (task.getTaskGroup() != null && task.getTaskGroup().getId() != null) {
            TaskGroup group = taskGroupRepository.findByIdAndUserId(task.getTaskGroup().getId(), user.getId())
                             .orElseThrow(() -> new RuntimeException("Task group not found or access denied"));
            task.setTaskGroup(group);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task taskUpdate, User user) {
        Task task = taskRepository.findById(taskId)
                                  .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (taskUpdate.getTitle() != null) {
            task.setTitle(taskUpdate.getTitle());
        }
        if (taskUpdate.getDescription() != null) {
            task.setDescription(taskUpdate.getDescription());
        }
        if (taskUpdate.getStatus() != null) {
            task.setStatus(taskUpdate.getStatus());
        }
        if (taskUpdate.getTaskGroup() != null && taskUpdate.getTaskGroup().getId() != null) {
            TaskGroup group = taskGroupRepository.findByIdAndUserId(taskUpdate.getTaskGroup().getId(), user.getId())
                    .orElseThrow(() -> new RuntimeException("Task group not found or access denied"));
            task.setTaskGroup(group);
        } else {
            task.setTaskGroup(null);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        taskRepository.delete(task);
    }

    public Map<TaskStatus, Long> getUserTaskStats(Long userId) {
        List<Object[]> stats = taskRepository.getTaskStatusStatsByUser(userId);
        return stats.stream()
                    .collect(Collectors.toMap(
                                      obj -> (TaskStatus) obj[0],
                                      obj -> (Long) obj[1]
                                              ));
    }

    public Map<String, Long> getUserTaskCountByGroup(Long userId) {
        List<Object[]> stats = taskRepository.getTaskCountByGroupForUser(userId);
        return stats.stream()
                    .collect(Collectors.toMap(
                                      obj -> (String) obj[0],
                                      obj -> (Long) obj[1]
                                              ));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getUserTasksById(Long userId) {
        return taskRepository.findByUserId(userId);
    }
}