package ua.khai.gorbatiuk.taskmanager.util.converter.resultset;

import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.Populator;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.ResultSetToCategoryPopulator;

import java.sql.ResultSet;

public class ResultSetToCategoryConverter implements Converter<ResultSet, Category> {

    private Populator<ResultSet, Category> categoryPopulator;

    public ResultSetToCategoryConverter() {
        categoryPopulator = new ResultSetToCategoryPopulator();
    }

    @Override
    public Category convert(ResultSet source) throws ConverterException {
        Category category = new Category();
        categoryPopulator.populate(source, category);
        return category;
    }
}
