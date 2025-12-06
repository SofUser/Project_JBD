package com.jbd.project_jbd.controller;

import com.jbd.project_jbd.dto.TaskRequest;
import com.jbd.project_jbd.entity.Task;
import com.jbd.project_jbd.entity.TaskStatus;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getUserTasks(@AuthenticationPrincipal User user) {
        return taskService.getUserTasks(user.getId());
    }

    @PostMapping
    public Task createTask(@RequestBody TaskRequest request, @AuthenticationPrincipal User user) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PLANNED);

        return taskService.createTask(task, user);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskRequest request, @AuthenticationPrincipal User user) {
        Task taskUpdate = new Task();
        taskUpdate.setTitle(request.getTitle());
        taskUpdate.setDescription(request.getDescription());
        taskUpdate.setStatus(request.getStatus());

        return taskService.updateTask(id, taskUpdate, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public Task updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, TaskStatus> request,
                                 @AuthenticationPrincipal User user) {
        Task taskUpdate = new Task();
        taskUpdate.setStatus(request.get("status"));

        return taskService.updateTask(id, taskUpdate, user);
    }

    @GetMapping("/stats")
    public Map<TaskStatus, Long> getUserTaskStats(@AuthenticationPrincipal User user) {
        return taskService.getUserTaskStats(user.getId());
    }

    @GetMapping("/group-stats")
    public Map<String, Long> getUserTaskCountByGroup(@AuthenticationPrincipal User user) {
        return taskService.getUserTaskCountByGroup(user.getId());
    }
}