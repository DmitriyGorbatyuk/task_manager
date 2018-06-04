package ua.khai.gorbatiuk.taskmanager.dao.impl;

import ua.khai.gorbatiuk.taskmanager.dao.CategoryDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoMysql implements CategoryDao {

    private static final String CATEGORY_COLUMNS = "`categories`.`id_category`, `categories`.`id_root`, `categories`.`name`," +
            " `categories`.`color`";
    private static final String CATEGORIES = "`task_manager`.`categories`";
    private static final String SELECT_ALL = "SELECT " + CATEGORY_COLUMNS + " FROM " + CATEGORIES;

    private ConnectionHolder connectionHolder;
    private Converter<ResultSet, Category> resultSetToCategoryConverter;

    public CategoryDaoMysql(ConnectionHolder connectionHolder, Converter<ResultSet, Category> resultSetToCategoryConverter) {
        this.connectionHolder = connectionHolder;
        this.resultSetToCategoryConverter = resultSetToCategoryConverter;
    }

    @Override
    public List<Category> getAll() {
        try(Statement statement = getConnection().createStatement()) {
            return getCategoriesFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    private List<Category> getCategoriesFromStatement(Statement statement) throws SQLException {
        List<Category> categories = new ArrayList<>();
        try(ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
            while (resultSet.next()) {
                categories.add(resultSetToCategoryConverter.convert(resultSet));
            }
        }
        return categories;
    }

    private Connection getConnection() {
        return connectionHolder.getConnection();
    }
}