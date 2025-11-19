package com.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConnectionManager {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER_CLASS;

    static {
        Properties props = new Properties();
        
        try (FileInputStream fis = new FileInputStream("database.properties")) {
            props.load(fis);
            
            DRIVER_CLASS = props.getProperty("driver");
            URL = props.getProperty("url");
            USER = props.getProperty("username");
            PASSWORD = props.getProperty("password");
            
            Class.forName(DRIVER_CLASS);
            
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL ERROR: MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("FATAL ERROR: database.properties file not found or failed to load.");
            e.printStackTrace();
        }
    }

    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}