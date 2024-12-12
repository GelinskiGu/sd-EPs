package com.gelinski.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static Connection conn = null;

    public static Connection connect() {
        if(conn != null) return conn;

        try {
            Properties props = loadProperties();
            String url = props.getProperty("url");

            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.err.println("ERR: Connection to the database: " + e.getLocalizedMessage());
        }

        return conn;
    }

    public static void disconnect() {
        if(conn == null) return;

        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
            System.err.println("ERR: Disconnect from the database: " + e.getLocalizedMessage());
        }

    }

    private static Properties loadProperties() {
        Properties props = new Properties();

        props.setProperty("user", "mysql");
        props.setProperty("password", "mysql");
        props.setProperty("url", "jdbc:mysql://localhost:3306/sd");
        props.setProperty("useSSL", "false");
        props.setProperty("allowPublicKeyRetrieval", "true");

        return props;
    }
}