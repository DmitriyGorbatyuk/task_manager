package ua.khai.gorbatiuk.taskmanager.dao.connection;


import ua.khai.gorbatiuk.taskmanager.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class MysqlTransactionManager {

    private ConnectionHolder connectionHolder;

    public MysqlTransactionManager(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public <T> T transact(Supplier<T> transaction) {
        Connection connection = null;
        T result = null;
        try {
            connection = prepareConnection();
            result = transaction.get();
            connection.commit();
            return result;
        } catch (SQLException e) {
            rollBack(connection);
            throw new DaoException(e);
        } finally {
            closeConnection(connection);
        }
    }

    private Connection prepareConnection() throws SQLException {
        connectionHolder.createConnection();
        Connection connection = connectionHolder.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    private void rollBack(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new DaoException("Cannot do rollback", e);
            }
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot close connection", e);
        }
        connectionHolder.removeConnection();
    }
}
