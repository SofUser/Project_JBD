package com.jbd.project_jbd.controller;

import com.jbd.project_jbd.entity.Task;
import com.jbd.project_jbd.entity.TaskGroup;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.service.TaskService;
import com.jbd.project_jbd.service.TaskGroupService;
import com.jbd.project_jbd.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;
    private final TaskGroupService taskGroupService;

    public AdminController(UserService userService, TaskService taskService, TaskGroupService taskGroupService) {
        this.userService = userService;
        this.taskService = taskService;
        this.taskGroupService = taskGroupService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/groups")
    public List<TaskGroup> getAllGroups() {
        return taskGroupService.getAllGroups();
    }

    @GetMapping("/users/{userId}/tasks")
    public List<Task> getUserTasks(@PathVariable Long userId) {
        return taskService.getUserTasksById(userId);
    }

    @GetMapping("/stats/tasks")
    public Object getTaskStats() {
        return Map.of(
                "totalTasks", taskService.getAllTasks().size(),
                "totalUsers", userService.getAllUsers().size(),
                "totalGroups", taskGroupService.getAllGroups().size()
        );
    }
}