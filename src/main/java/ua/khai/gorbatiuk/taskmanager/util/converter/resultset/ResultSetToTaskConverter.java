package ua.khai.gorbatiuk.taskmanager.util.converter.resultset;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.entity.model.Task;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.constant.Table;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.Populator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ResultSetToTaskConverter implements Converter<ResultSet, Task> {

    private Populator<ResultSet, Category> categoryPopulator;

    public ResultSetToTaskConverter(Populator<ResultSet, Category> categoryPopulator) {
        this.categoryPopulator = categoryPopulator;
    }

    @Override
    public Task convert(ResultSet source) throws ConverterException {
        try{
            Task task = new Task();
            task.setId(source.getInt(Table.Task.ID));
            task.setRootId(source.getInt(Table.Task.ID_ROOT));

            String date = source.getString(Table.Task.DATE);
            if(date == null) {
                task.setDate(LocalDateTime.now().withNano(0).withSecond(0));
            } else {
                task.setDate(LocalDateTime.parse(date.toString()));
            }


            task.setTime(source.getInt(Table.Task.TIME));
            task.setComplexity(source.getInt(Table.Task.COMPLEXITY));
            task.setDescription(source.getString(Table.Task.TEXT));
            task.setName(source.getString(Table.Task.NAME));
            task.setChecked(source.getBoolean(Table.Task.CHECKED));

            Category category = new Category();
            categoryPopulator.populate(source, category);
            task.setCategory(category);

            User user = new User();
            user.setId(source.getInt(Table.Task.USER));
            task.setUser(user);

            return task;
        } catch (SQLException e) {
            throw new ConverterException("Cannot convert ResultSet to Task",e);
        }
    }
}
