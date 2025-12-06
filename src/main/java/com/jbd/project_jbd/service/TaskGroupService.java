package com.jbd.project_jbd.service;

import com.jbd.project_jbd.entity.TaskGroup;
import com.jbd.project_jbd.entity.User;
import com.jbd.project_jbd.repository.TaskGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;

    public TaskGroupService(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    public List<TaskGroup> getUserGroups(Long userId) {
        return taskGroupRepository.findByUserId(userId);
    }

    public TaskGroup createGroup(String name, User user) {
        TaskGroup group = new TaskGroup(name, user);
        return taskGroupRepository.save(group);
    }

    public TaskGroup updateGroup(Long groupId, String name, User user) {
        TaskGroup group = taskGroupRepository.findByIdAndUserId(groupId, user.getId())
                           .orElseThrow(() -> new RuntimeException("Task group not found or access denied"));

        group.setName(name);
        return taskGroupRepository.save(group);
    }

    public void deleteGroup(Long groupId, User user) {
        TaskGroup group = taskGroupRepository.findByIdAndUserId(groupId, user.getId())
                           .orElseThrow(() -> new RuntimeException("Task group not found or access denied"));

        taskGroupRepository.delete(group);
    }

    public List<TaskGroup> getAllGroups() {
        return taskGroupRepository.findAll();
    }
}