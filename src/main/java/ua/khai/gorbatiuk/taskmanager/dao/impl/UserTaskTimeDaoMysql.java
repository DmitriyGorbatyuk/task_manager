package ua.khai.gorbatiuk.taskmanager.dao.impl;

import ua.khai.gorbatiuk.taskmanager.dao.AbstractMysqlDao;
import ua.khai.gorbatiuk.taskmanager.dao.UserTaskTimeDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.entity.bean.UserTaskTime;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTaskTimeDaoMysql  extends AbstractMysqlDao implements UserTaskTimeDao {

    private static final String TABLE_NAME = "`task_manager`.`executing_tasks`";
    private static final String INSERT_TIME = "INSERT INTO " + TABLE_NAME + " (`id_task`, `id_user`, `start_time`) VALUES (?, ?, ?);";
    private static final String SELECT_BY_USER = "SELECT * FROM " + TABLE_NAME + " where `id_user` = ?";
    private static final String DELETE_BY_USER = "DELETE FROM " + TABLE_NAME + " WHERE `id_user`= ?";

    private Converter<ResultSet, UserTaskTime> resultSetToUserTaskTimeConverter;

    public UserTaskTimeDaoMysql(ConnectionHolder connectionHolder,
                                Converter<ResultSet, UserTaskTime> resultSetToUserTaskTimeConverter) {
        super(connectionHolder);
        this.resultSetToUserTaskTimeConverter = resultSetToUserTaskTimeConverter;
    }

    @Override
    public UserTaskTime getStartTimeByUserId(Integer userId) {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USER)) {
            int k = 1;
            statement.setInt(k++, userId);
            return getUserTaskTimeFromStatement(statement);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private UserTaskTime getUserTaskTimeFromStatement(PreparedStatement statement) throws SQLException {
        try(ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next()) {
                return resultSetToUserTaskTimeConverter.convert(resultSet);
            }
        }
        return null;
    }

    @Override
    public int setStartTime(UserTaskTime userTaskTime) {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_TIME)) {
            int k = 1;
            statement.setInt(k++, userTaskTime.getTaskId());
            statement.setInt(k++, userTaskTime.getUserId());
            statement.setString(k++, userTaskTime.getTime().toString());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int deleteStartTime(UserTaskTime userTaskTime) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_BY_USER)) {
            int k = 1;
            statement.setInt(k++, userTaskTime.getUserId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
