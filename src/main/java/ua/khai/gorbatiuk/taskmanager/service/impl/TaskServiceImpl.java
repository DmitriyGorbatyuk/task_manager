package ua.khai.gorbatiuk.taskmanager.service.impl;

import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private MysqlTransactionManager transactionManager;

    private TaskDao taskDao;

    public TaskServiceImpl(MysqlTransactionManager transactionManager, TaskDao taskDao) {
        this.transactionManager = transactionManager;
        this.taskDao = taskDao;
    }

    @Override
    public List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer rootTaskId) {
        try {
            return transactionManager.transact(() -> {
                List<Task> tasks = taskDao.getAllByUserIdAndRootTaskId(userId, rootTaskId);
                if (tasks.size() > 0) {
                    return tasks;
                }
                throw new WrongUserDataException("There is no tasks with rootTaskId=" + rootTaskId);
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Task getByUserIdAndTaskId(Integer userId, Integer taskId) {
        try {
            return transactionManager.transact(() -> {
                Task task = taskDao.getByUserIdAndTaskId(userId, taskId);
                if (task != null) {
                    return task;
                }
                throw new WrongUserDataException("There is no task with taskId=" + taskId);
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
