package ua.khai.gorbatiuk.taskmanager.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {
    private static final ThreadLocal<Connection> threadLocalScope = new  ThreadLocal<>();

    private MysqlConnectionManager connectionManager;

    public ConnectionHolder(MysqlConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public Connection getConnection() {
        return threadLocalScope.get();
    }

    public void createConnection() throws SQLException {
        threadLocalScope.set(connectionManager.getConnection());
    }
    public void removeConnection() {
        threadLocalScope.remove();
    }
}
