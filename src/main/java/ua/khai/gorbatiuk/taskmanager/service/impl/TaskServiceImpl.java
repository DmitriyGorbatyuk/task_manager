package ua.khai.gorbatiuk.taskmanager.service.impl;

import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.UserTaskTimeDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.entity.bean.UserTaskTime;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    private MysqlTransactionManager transactionManager;

    private TaskDao taskDao;
    private UserTaskTimeDao userTaskTimeDao;

    public TaskServiceImpl(MysqlTransactionManager transactionManager, TaskDao taskDao, UserTaskTimeDao userTaskTimeDao) {
        this.transactionManager = transactionManager;
        this.taskDao = taskDao;
        this.userTaskTimeDao = userTaskTimeDao;
    }


    @Override
    public List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer rootTaskId) {
        try {
            return transactionManager.transact(() -> {
                List<Task> tasks = taskDao.getAllByUserIdAndRootTaskId(userId, rootTaskId);
                if (!tasks.isEmpty()) {
                    for (Task task : tasks) {
                        readTimeRecursively(task);
                    }
                    return tasks;
                }
                throw new WrongUserDataException("There are not tasks with rootTaskId=" + rootTaskId);

            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void readTimeRecursively(Task currentTask) {
        List<Task> children = taskDao.getAllByUserIdAndRootTaskId(currentTask.getUser().getId(), currentTask.getId());
        for (Task child : children) {
            readTimeRecursively(child);
        }
        currentTask.setIsLeaf(!children.stream().anyMatch(item -> !item.getChecked()));
        if (!children.isEmpty()) {
            currentTask.setTime(children.stream().mapToInt(item -> item.getTime()).sum());
        }
    }

    @Override
    public Task getByUserIdAndTaskId(Integer userId, Integer taskId) {
        try {
            return transactionManager.transact(() -> {
                Task task = taskDao.getByUserIdAndTaskId(userId, taskId);
                if (task != null) {
                    readTimeRecursively(task);
                    return task;
                }
                throw new WrongUserDataException("There is not task with taskId=" + taskId);
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void add(Task newTask) {
        try {
            transactionManager.transact(() -> {
                Task rootTask = taskDao.getByUserIdAndTaskId(newTask.getUser().getId(), newTask.getRootId());
                newTask.setCategory(rootTask.getCategory());
                newTask.setDate(LocalDateTime.now());
                taskDao.add(newTask);
                return 0;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Task updatedTask) {
        try {
            transactionManager.transact(() -> {
                Integer updatedLines = taskDao.update(updatedTask);
                if (updatedLines != null && updatedLines == 1) {
                    return updatedLines;
                }
                throw new ServiceException("There is more updated tasks lines than 1(" + updatedLines + ")");
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void executeTask(Task task) {
        try {
            transactionManager.transact(() -> {
                UserTaskTime userTaskTime = userTaskTimeDao.getStartTimeByUserId(task.getUser().getId());
                if (userTaskTime == null) {
                    startExecuteTask(task);
                } else {
                    finishExecuteTask(userTaskTime);
                    if (!task.getId().equals(userTaskTime.getTaskId())) {
                        startExecuteTask(task);
                    }
                }
                return null;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private void startExecuteTask(Task task) {
        UserTaskTime userTaskTime = new UserTaskTime();

        userTaskTime.setUserId(task.getUser().getId());
        userTaskTime.setTaskId(task.getId());
        userTaskTime.setTime(LocalDateTime.now().withNano(0));

        userTaskTimeDao.setStartTime(userTaskTime);
    }

    private void finishExecuteTask(UserTaskTime userTaskTime) {
        LocalDateTime finishTime = LocalDateTime.now().withNano(0);
        Long seconds = Duration.between(userTaskTime.getTime(), finishTime).getSeconds();

        Task currentTask = taskDao.getByUserIdAndTaskId(userTaskTime.getUserId(), userTaskTime.getTaskId());

        currentTask.setTime(currentTask.getTime() + seconds.intValue());

        taskDao.update(currentTask);
        userTaskTimeDao.deleteStartTime(userTaskTime);
    }

    @Override
    public void checkTask(Task task) {
        try {
            transactionManager.transact(() -> {
                task.setChecked(!task.getChecked());
                Integer updatedLines = taskDao.update(task);
                if (updatedLines != null && updatedLines == 1) {
                    return updatedLines;
                }
                throw new ServiceException("There is more updated tasks lines than 1(" + updatedLines + ")");
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
