package com.dataaccess.finalactivityMMR2627;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseManager {
    private static Connection connection;

    public static Connection getConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        Properties props = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new Exception("Cannot find db.properties file in resources!");
            }
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        Class.forName("org.postgresql.Driver");
        Connection newConnection = DriverManager.getConnection(url, user, password);
        try {
            createAvailableFlightsFunction(newConnection);
            connection = newConnection;
        } catch (Exception e) {
            newConnection.close();
            throw e;
        }
        return connection;
    }

    private static void createAvailableFlightsFunction(Connection connection) throws Exception {
        try (InputStream input = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("available_flights_mmr.sql")) {
            if (input == null) {
                throw new Exception("Cannot find available_flights_mmr.sql in resources.");
            }
            String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        }
    }

    public static boolean isDatabaseAvailable() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}