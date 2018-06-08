package ua.khai.gorbatiuk.taskmanager.util.converter.populator;

import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.exception.PopulatorException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskToPreparedStatementPopulator implements Populator<Task, PreparedStatement> {
    @Override
    public void populate(Task source, PreparedStatement target) throws PopulatorException {
        try {
            int k=1;
            target.setString(k++, source.getName());
            target.setString(k++, source.getDate().toString());
            target.setInt(k++, source.getComplexity());
            target.setString(k++, source.getDescription());
            target.setInt(k++, source.getTime());
            target.setInt(k++, source.getCategory().getId());
            target.setInt(k++, source.getId());
            target.setInt(k++, source.getUser().getId());
        } catch (SQLException e) {
            throw new PopulatorException("Cannot convert Task to PreparedStatement", e);
        }


    }
}
