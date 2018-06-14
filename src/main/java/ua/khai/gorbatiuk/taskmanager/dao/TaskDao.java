package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.model.Task;

import java.util.List;

public interface TaskDao {
    List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer taskId);
    Task getByUserIdAndTaskId(Integer userId, Integer taskId);
    void add(Task newTask);
    Integer update(Task updatedTask);

    List<Task> getAllByUserId(Integer userId);
}
