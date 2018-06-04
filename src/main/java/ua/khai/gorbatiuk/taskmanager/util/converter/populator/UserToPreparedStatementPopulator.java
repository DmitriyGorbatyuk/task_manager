package ua.khai.gorbatiuk.taskmanager.util.converter.populator;


import ua.khai.gorbatiuk.taskmanager.exception.PopulatorException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserToPreparedStatementPopulator implements Populator<User, PreparedStatement> {
    @Override
    public void populate(User source, PreparedStatement target) {
        try {
            int k = 1;
            target.setString(k++, source.getEmail());
            target.setString(k++, source.getPassword());
        } catch (SQLException e) {
            throw new PopulatorException("Cannot convert User to PreparedStatement", e);
        }
    }
}
