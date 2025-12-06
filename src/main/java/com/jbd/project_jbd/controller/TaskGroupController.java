package com.jbd.project_jbd.controller;

import com.jbd.project_jbd.entity.TaskGroup;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.service.TaskGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class TaskGroupController {

    private final TaskGroupService taskGroupService;

    public TaskGroupController(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    @GetMapping
    public List<TaskGroup> getUserGroups(@AuthenticationPrincipal User user) {
        return taskGroupService.getUserGroups(user.getId());
    }

    @PostMapping
    public TaskGroup createGroup(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        return taskGroupService.createGroup(request.get("name"), user);
    }

    @PutMapping("/{id}")
    public TaskGroup updateGroup(@PathVariable Long id, @RequestBody Map<String, String> request,
                                 @AuthenticationPrincipal User user) {
        return taskGroupService.updateGroup(id, request.get("name"), user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskGroupService.deleteGroup(id, user);
        return ResponseEntity.ok().build();
    }
}