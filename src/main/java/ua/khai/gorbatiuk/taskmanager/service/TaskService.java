package ua.khai.gorbatiuk.taskmanager.service;

import ua.khai.gorbatiuk.taskmanager.entity.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer rootTaskId);
    Task getByUserIdAndTaskId(Integer userId, Integer rootTaskId);
}
