package ua.khai.gorbatiuk.taskmanager.util.converter.populator;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.PopulatorException;
import ua.khai.gorbatiuk.taskmanager.util.constant.Table;

import javax.swing.text.TabableView;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToCategoryPopulator implements Populator<ResultSet, Category> {
    @Override
    public void populate(ResultSet source, Category target) throws PopulatorException {
        try {
            target.setId(source.getInt(Table.Category.ID));
            target.setIdRoot(source.getInt(Table.Category.ID_ROOT));
            target.setName(source.getString(Table.Category.NAME));
            target.setColor(source.getString(Table.Category.COLOR));
        } catch (SQLException e) {
            throw new PopulatorException("Cannot populate Category from ResultSet", e);
        }

    }
}
