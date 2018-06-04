package ua.khai.gorbatiuk.taskmanager.util.converter.resultset;


import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserConverter implements Converter<ResultSet, User> {
    @Override
    public User convert(ResultSet source) {
        try {
            User user = new User();
            user.setId(source.getInt("id_user"));
            user.setEmail(source.getString("email"));
            user.setPassword(source.getString("password"));
            return user;
        } catch (SQLException e) {
            throw new ConverterException("Cannot convert ResultSet to User", e);
        }
    }
}
