package ua.khai.gorbatiuk.taskmanager.dao.impl;

import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoMySql implements TaskDao {

    private static final String TASK_COLUMNS = "`tasks`.`id_task`, `tasks`.`id_root`, `tasks`.`name`, `tasks`.`date`, " +
            "`tasks`.`complexity`, `tasks`.`text`, `tasks`.`time`, `tasks`.`checked`, `tasks`.`fk_user` ";
    private static final String CATEGORY_COLUMNS = "`categories`.`id_category`, `categories`.`id_root`, `categories`.`name`," +
            " `categories`.`color`";
    private static final String TASKS = "`task_manager`.`tasks`";
    private static final String CATEGORIES = "`task_manager`.`categories`";
    private static final String SELECT_BY_USERID = "SELECT " + TASK_COLUMNS + ", " + CATEGORY_COLUMNS + " FROM " + TASKS +
            " INNER JOIN " + CATEGORIES + " ON `tasks`.`fk_category` = `categories`.`id_category`" + " where fk_user = ?";
    private static final String SELECT_BY_USERID_ROOTID = SELECT_BY_USERID + " and `tasks`.`id_root` = ?";
    private static final String SELECT_BY_USERID_TASKID = SELECT_BY_USERID + " and `tasks`.`id_task` = ?";

    private ConnectionHolder connectionHolder;
    private Converter<ResultSet, Task> resultSetToTaskConverter;

    public TaskDaoMySql(ConnectionHolder connectionHolder, Converter<ResultSet, Task> resultSetToTaskConverter) {
        this.connectionHolder = connectionHolder;
        this.resultSetToTaskConverter = resultSetToTaskConverter;
    }


    @Override
    public List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer taskRootId) {
        try(PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USERID_ROOTID)) {
            int k = 1;
            statement.setInt(k++, userId);
            statement.setInt(k++, taskRootId);
            return getTasksFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    private List<Task> getTasksFromStatement(PreparedStatement statement) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        try(ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tasks.add(resultSetToTaskConverter.convert(resultSet));
            }
        }
        return tasks;
    }

    @Override
    public Task getByUserIdAndTaskId(Integer userId, Integer taskId) {
        try(PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USERID_TASKID)) {
            int k = 1;
            statement.setInt(k++, userId);
            statement.setInt(k++, taskId);
            try(ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetToTaskConverter.convert(resultSet);
                }
            }
            return null;
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }


    private Connection getConnection() {
        return connectionHolder.getConnection();
    }
}
