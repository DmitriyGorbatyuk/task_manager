package ua.khai.gorbatiuk.taskmanager.dao.connection;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlConnectionManager {

    private static final Logger logger = Logger.getLogger(MysqlConnectionManager.class);

    private DataSource dataSource;

    public MysqlConnectionManager(String dbName) {
        try {
            Context initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup(dbName);
        } catch (NamingException e) {
            logger.debug(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
