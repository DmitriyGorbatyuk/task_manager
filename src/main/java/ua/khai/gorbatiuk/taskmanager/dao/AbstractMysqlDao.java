package ua.khai.gorbatiuk.taskmanager.dao;

import ua.khai.gorbatiuk.taskmanager.dao.connection.ConnectionHolder;

import java.sql.Connection;

public class AbstractMysqlDao {

    private ConnectionHolder connectionHolder;

    public AbstractMysqlDao(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    protected Connection getConnection() {
        return connectionHolder.getConnection();
    }
}
