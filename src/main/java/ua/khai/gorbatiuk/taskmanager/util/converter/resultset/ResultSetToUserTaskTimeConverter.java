package ua.khai.gorbatiuk.taskmanager.util.converter.resultset;

import ua.khai.gorbatiuk.taskmanager.entity.bean.UserTaskTime;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ResultSetToUserTaskTimeConverter implements Converter<ResultSet, UserTaskTime> {
    @Override
    public UserTaskTime convert(ResultSet source) throws ConverterException {
        try{
            UserTaskTime userTaskTime = new UserTaskTime();

            userTaskTime.setTaskId(source.getInt("id_task"));
            userTaskTime.setUserId(source.getInt("id_user"));
            userTaskTime.setTime(LocalDateTime.parse(source.getString("start_time")));

            return userTaskTime;
        } catch (SQLException e) {
            throw new ConverterException("Cannot convert ResultSet to Task",e);
        }
    }
}
