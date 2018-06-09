package ua.khai.gorbatiuk.taskmanager.dao.impl;

import ua.khai.gorbatiuk.taskmanager.dao.AbstractMysqlDao;
import ua.khai.gorbatiuk.taskmanager.dao.TaskDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.Populator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoMySql extends AbstractMysqlDao implements TaskDao {

    private static final String TASK_COLUMNS = "`tasks`.`id_task`, `tasks`.`id_root`, `tasks`.`name`, `tasks`.`date`, " +
            "`tasks`.`complexity`, `tasks`.`text`, `tasks`.`time`, `tasks`.`checked`, `tasks`.`fk_user` ";
    private static final String CATEGORY_COLUMNS = "`categories`.`id_category`, `categories`.`id_root`, `categories`.`name`," +
            " `categories`.`color`";
    private static final String TASKS = "`task_manager`.`tasks`";
    private static final String CATEGORIES = "`task_manager`.`categories`";
    private static final String ORDER_BY_TASKS_ID_TASK = "ORDER BY `tasks`.`checked`, `tasks`.`id_task` desc ";

    private static final String SELECT_BY_USERID = "SELECT " + TASK_COLUMNS + ", " + CATEGORY_COLUMNS + " FROM " + TASKS +
            " INNER JOIN " + CATEGORIES + " ON `tasks`.`fk_category` = `categories`.`id_category`" + " where fk_user = ?";
    private static final String SELECT_BY_USERID_ROOTID = SELECT_BY_USERID + " and `tasks`.`id_root` = ? " + ORDER_BY_TASKS_ID_TASK;
    private static final String SELECT_BY_USERID_TASKID = SELECT_BY_USERID + " and `tasks`.`id_task` = ? ";

    private static final String ADD_TASK = "INSERT INTO " + TASKS +
            " (`id_task`, `id_root`, `name`, `fk_user`, `date`, `fk_category`) " +
            "values(default, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TASK = "UPDATE " + TASKS + " SET `name` = ?, `date` = ?, `complexity` = ?, " +
            "`text` = ?, `time` = ?, `checked` = ? ,`fk_category` = ? where `id_task` = ? AND `fk_user` = ?";


    private Converter<ResultSet, Task> resultSetToTaskConverter;
    private Populator<Task, PreparedStatement> taskToPreparedStatementPopulator;

    public TaskDaoMySql(ConnectionHolder connectionHolder, Converter<ResultSet, Task> resultSetToTaskConverter,
                        Populator<Task, PreparedStatement> taskToPreparedStatementPopulator) {
        super(connectionHolder);
        this.resultSetToTaskConverter = resultSetToTaskConverter;
        this.taskToPreparedStatementPopulator = taskToPreparedStatementPopulator;
    }

    @Override
    public List<Task> getAllByUserIdAndRootTaskId(Integer userId, Integer taskRootId) {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USERID_ROOTID)) {
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
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tasks.add(resultSetToTaskConverter.convert(resultSet));
            }
        }
        return tasks;
    }

    @Override
    public Task getByUserIdAndTaskId(Integer userId, Integer taskId) {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USERID_TASKID)) {
            int k = 1;
            statement.setInt(k++, userId);
            statement.setInt(k++, taskId);
            return getTaskFromPrepareStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    private Task getTaskFromPrepareStatement(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSetToTaskConverter.convert(resultSet);
            }
        }
        return null;
    }

    @Override
    public void add(Task newTask) {
        try (PreparedStatement statement = getConnection().prepareStatement(ADD_TASK)) {
            int k = 1;

            statement.setInt(k++, newTask.getRootId());
            statement.setString(k++, newTask.getName());
            statement.setInt(k++, newTask.getUser().getId());
            statement.setString(k++, newTask.getDate().toString());
            statement.setInt(k++, newTask.getCategory().getId());

            statement.execute();
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Integer update(Task updatedTask) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_TASK)) {
            taskToPreparedStatementPopulator.populate(updatedTask, statement);
            return statement.executeUpdate();
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }
}
