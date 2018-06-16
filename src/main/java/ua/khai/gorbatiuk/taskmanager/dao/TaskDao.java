package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.entity.model.Task;

import java.util.List;

public interface TaskDao {
    List<Task> getAllByUserId(Integer userId);
    List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer taskId);
    List<Task> getAllByCategoryId(Integer id);
    Task getByUserIdAndTaskId(Integer userId, Integer taskId);
    Task add(Task newTask);
    Integer update(Task updatedTask);
    Integer delete(Integer taskId, Integer userId);
}
