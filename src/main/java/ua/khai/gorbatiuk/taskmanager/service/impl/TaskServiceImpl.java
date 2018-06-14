package ua.khai.gorbatiuk.taskmanager.service.impl;

import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.UserTaskTimeDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.MysqlTransactionManager;
import ua.khai.gorbatiuk.taskmanager.entity.bean.TasksBean;
import ua.khai.gorbatiuk.taskmanager.entity.bean.UserTaskTime;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.ServiceException;
import ua.khai.gorbatiuk.taskmanager.exception.WrongUserDataException;
import ua.khai.gorbatiuk.taskmanager.service.TaskService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskServiceImpl implements TaskService {

    private final Map<String, Function<Task, Comparable>> sortedField = new HashMap<>();

    private MysqlTransactionManager transactionManager;

    private TaskDao taskDao;
    private UserTaskTimeDao userTaskTimeDao;

    public TaskServiceImpl(MysqlTransactionManager transactionManager, TaskDao taskDao, UserTaskTimeDao userTaskTimeDao) {
        this.transactionManager = transactionManager;
        this.taskDao = taskDao;
        this.userTaskTimeDao = userTaskTimeDao;
        sortedField.put("name", Task::getName);
        sortedField.put("complexity", Task::getComplexity);
        sortedField.put("category", task -> task.getCategory().getName());
        sortedField.put("time", Task::getTime);
        sortedField.put("date", Task::getDate);
    }

    @Override
    public List<Task> getAllByUserId(Integer userId) {
        try {
            return transactionManager.transact(() -> {
                List<Task> tasks = taskDao.getAllByUserIdAndRootTaskId(userId, 1);
                if (!tasks.isEmpty()) {
                    for (Task task : tasks) {
                        readTimeRecursively(task);
                    }
                    return tasks;
                }
                throw new WrongUserDataException("There are not task with userId=" + userId);

            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Task> getUncheckedByUserIdAndRootTaskId(Integer userId, Integer rootTaskId) {
        try {
            return transactionManager.transact(() -> {
                List<Task> tasks = taskDao.getAllByUserIdAndRootTaskId(userId, rootTaskId);
                if (!tasks.isEmpty()) {
                    List<Task> returned = new ArrayList<>();
                    tasks.stream().filter(task -> !task.getChecked()).forEach(task -> returned.add(task));
                    return returned;
                }
                throw new WrongUserDataException("There are not task with rootTaskId=" + rootTaskId);

            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    @Override
    public List<Task> getAllByTasksBean(TasksBean tasksBean) {
        try {
            return transactionManager.transact(() -> {
                List<Task> tasks = taskDao.getAllByUserIdAndRootTaskId(tasksBean.getUser().getId(), tasksBean.getTask().getId());
                if (!tasks.isEmpty()) {
                    for (Task task : tasks) {
                        readTimeRecursively(task);
                    }
                    sortTasks(tasks, tasksBean.getSortField(), tasksBean.getAscending());
                    return tasks;
                }
                throw new WrongUserDataException("There are not task with rootTaskId=" + tasksBean.getTask().getRootId());

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


    private void sortTasks(List<Task> tasks, String sortField, Boolean ascending) {
        if (sortField != null && !sortField.equals("none")) {
            sortTasksByField(tasks, sortedField.get(sortField), ascending);
        }
    }

    private void sortTasksByField(List<Task> tasks, Function<Task, Comparable> method, Boolean ascending) {
        if (ascending) {
            tasks.sort(Comparator.comparing(method));
        } else {
            tasks.sort(Comparator.comparing(method).reversed());
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
                newTask.setDate(LocalDateTime.now().withSecond(0).withNano(0));
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
                throw new ServiceException("There is more updated task lines than 1(" + updatedLines + ")");
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
                throw new ServiceException("There is more updated task lines than 1(" + updatedLines + ")");
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Task> getTodayTasks(TasksBean tasksBean) {
        try {
            return transactionManager.transact(() -> {
                List<Task> allTasks = taskDao.getAllByUserId(tasksBean.getUser().getId());
                LocalDate today = LocalDate.now();
                List<Task> todays = allTasks.stream().filter(task -> task.getDate() != null &&
                        task.getDate().toLocalDate().equals(today)).collect(Collectors.toList());
                todays.stream().forEach(task -> readTimeRecursively(task));
                sortTasks(todays, tasksBean.getSortField(), tasksBean.getAscending());
                return todays;
            });
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
