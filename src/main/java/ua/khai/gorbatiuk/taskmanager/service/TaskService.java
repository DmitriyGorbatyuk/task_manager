package ua.khai.gorbatiuk.taskmanager.service;

import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getUncheckedByUserIdAndRootTaskId(Integer userId, Integer rootTaskId);
    List<Task> getAllByTasksBean(TasksBean tasksBean);
    Task getByUserIdAndTaskId(Integer userId, Integer rootTaskId);
    void add(Task newTask);
    void update(Task updatedTask);
    void executeTask(Task task);
    void checkTask(Task task);
    List<Task> getAllByUserId(Integer userId);

    List<Task> getTodayTasks(TasksBean tasksBean);
}
