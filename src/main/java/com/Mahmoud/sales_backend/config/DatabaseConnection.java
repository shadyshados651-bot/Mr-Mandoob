package com.Mahmoud.sales_backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // instance واحدة فقط
    private static DatabaseConnection instance;

    private Connection connection;

    // بيانات الاتصال
    private static final String URL = "jdbc:mysql://localhost:3306/mandoob2";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Constructor private عشان ممنوع إنشاء object من بره
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Singleton method
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // ترجع الـ connection
    public Connection getConnection() {
        return connection;
    }
}