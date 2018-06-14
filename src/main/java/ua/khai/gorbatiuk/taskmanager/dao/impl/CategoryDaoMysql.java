package ua.khai.gorbatiuk.taskmanager.dao.impl;

import ua.khai.gorbatiuk.taskmanager.dao.AbstractMysqlDao;
import ua.khai.gorbatiuk.taskmanager.dao.CategoryDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.entity.model.Category;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoMysql extends AbstractMysqlDao implements CategoryDao {

    private static final String CATEGORY_COLUMNS = "`categories`.`id_category`, `categories`.`id_root`, `categories`.`name`," +
            " `categories`.`color`";
    private static final String CATEGORIES = "`task_manager`.`categories`";
    private static final String SELECT_BY_USERID = "SELECT " + CATEGORY_COLUMNS + " FROM " + CATEGORIES + "where `categories`.`fk_user` = ? ";
    private static final String SELECT_BY_CATEGORYID = SELECT_BY_USERID + "and id_category = ? ";
    private static final String SELECT_BY_CATEGORYROOTID = SELECT_BY_USERID + "and id_root = ? ";

    private Converter<ResultSet, Category> resultSetToCategoryConverter;

    public CategoryDaoMysql(ConnectionHolder connectionHolder, Converter<ResultSet, Category> resultSetToCategoryConverter) {
        super(connectionHolder);
        this.resultSetToCategoryConverter = resultSetToCategoryConverter;
    }


    @Override
    public List<Category> getALlByUserIdAndCategoryRootId(Integer userId, Integer categoryId) {
        try(PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_CATEGORYROOTID)) {
            int k = 1;
            statement.setInt(k++, userId);
            statement.setInt(k++, categoryId);
            return getCategoriesFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Category> getAllByUserId(Integer userId) {
        try(PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_USERID)) {
            statement.setInt(1, userId);
            return getCategoriesFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    private List<Category> getCategoriesFromStatement(PreparedStatement statement) throws SQLException {
        List<Category> categories = new ArrayList<>();
        try(ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(resultSetToCategoryConverter.convert(resultSet));
            }
        }
        return categories;
    }

    @Override
    public Category getByUserIdAndCategoryId(Integer userId, Integer categoryId) {
        try(PreparedStatement statement = getConnection().prepareStatement(SELECT_BY_CATEGORYID)) {
            int k = 1;
            statement.setInt(k++, userId);
            statement.setInt(k++, categoryId);
            return getCategoryFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    private Category getCategoryFromStatement(PreparedStatement statement) throws SQLException {
        Category category = new Category();
        try(ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                category = resultSetToCategoryConverter.convert(resultSet);
            }
        }
        return category;
    }

    @Override
    public void add(Integer id, Integer rootId, String categoryName) {
        try(PreparedStatement statement = getConnection().prepareStatement()) {
            int k = 1;
            statement.setInt(k++, id);
            statement.setInt(k++, rootId);
            statement.setString(k++, categoryName);
            statement.executeUpdate();
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer id, Integer rootId) {
        try(PreparedStatement statement = getConnection().prepareStatement()) {
            int k = 1;
            statement.setInt(k++, id);
            statement.setInt(k++, rootId);
            statement.executeUpdate();
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Category category) {
        try(PreparedStatement statement = getConnection().prepareStatement()) {
            int k = 1;
            statement.setInt(k++, category.getId());
            statement.setInt(k++, category.getRootId());
            statement.setString(k++, category.getName());
            statement.setString(k++, category.getColor());
            statement.executeUpdate();
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }
}
