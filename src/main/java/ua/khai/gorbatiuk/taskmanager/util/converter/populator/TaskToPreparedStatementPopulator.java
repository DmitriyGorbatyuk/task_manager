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
            if(source.getDate() == null) {
                target.setNull(k++, java.sql.Types.VARCHAR);
            } else {
                target.setString(k++, source.getDate().toString());
            }

            target.setInt(k++, source.getComplexity());
            target.setString(k++, source.getDescription());
            target.setInt(k++, source.getTime());
            target.setBoolean(k++, source.getChecked());
            target.setInt(k++, source.getCategory().getId());
            target.setInt(k++, source.getId());
            target.setInt(k++, source.getUser().getId());
        } catch (SQLException e) {
            throw new PopulatorException("Cannot convert Task to PreparedStatement", e);
        }


    }
}
