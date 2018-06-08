package ua.khai.gorbatiuk.taskmanager.dao.impl;


import ua.khai.gorbatiuk.taskmanager.dao.AbstractMysqlDao;
import ua.khai.gorbatiuk.taskmanager.util.converter.Converter;
import ua.khai.gorbatiuk.taskmanager.util.converter.populator.Populator;
import ua.khai.gorbatiuk.taskmanager.dao.UserDao;
import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;
import ua.khai.gorbatiuk.taskmanager.exception.ConverterException;
import ua.khai.gorbatiuk.taskmanager.exception.DaoException;
import ua.khai.gorbatiuk.taskmanager.exception.PopulatorException;
import ua.khai.gorbatiuk.taskmanager.entity.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOMysql extends AbstractMysqlDao implements UserDao {

    private static final String USER_COLUMNS = "`id_user`, `email`, `password`";
    private static final String TABLE_NAME = "`task_manager`.`users`";
    private static final String SELECT_USERS_BY_EMAIL = "SELECT " + USER_COLUMNS + " FROM " + TABLE_NAME + " where email = ?";
    private static final String INSERT_USER = "INSERT INTO " + TABLE_NAME + "(" + USER_COLUMNS + ") VALUES (default, ?, ?)";


    private Populator<User, PreparedStatement> userToPreparedStatementPopulator;
    private Converter<ResultSet, User> resultSetToUserConverter;

    public UserDAOMysql(ConnectionHolder connectionHolder,
                        Populator<User, PreparedStatement> userToPreparedStatementPopulator,
                        Converter<ResultSet, User> resultSetToUserConverter) {
        super(connectionHolder);
        this.userToPreparedStatementPopulator = userToPreparedStatementPopulator;
        this.resultSetToUserConverter = resultSetToUserConverter;
    }

    @Override
    public User add(User newUser) {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            userToPreparedStatementPopulator.populate(newUser, statement);
            statement.execute();
            updateUserId(newUser, statement);
            return newUser;
        } catch (SQLException | PopulatorException e) {
            throw new DaoException(e);
        }
    }

    private void updateUserId(User newUser, PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                newUser.setId(id);
            }
        }
    }

    private User getUserFromStatement(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if(resultSet.next()) {
                return resultSetToUserConverter.convert(resultSet);
            }
            return null;
        }
    }

    @Override
    public User getByEmail(String email) {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_USERS_BY_EMAIL)) {
            statement.setString(1, email);
            return getUserFromStatement(statement);
        } catch (SQLException | ConverterException e) {
            throw new DaoException(e);
        }
    }
}
