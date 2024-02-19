package it.unibs.pajc.lithium.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteInterface extends DBInterface implements Closeable {
    private Connection connection;

    public void connect(String dbUrl) {
        try {
            connection = DriverManager.getConnection(dbUrl);
            System.out.println("Connection to db established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String testQuery() {
        try {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM album");
            var stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                stringBuilder.append(resultSet.getString("title"));
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean authenticateUser(String username, String passwordHash) {
        // TODO: implement
        return false;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}