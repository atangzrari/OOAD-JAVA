package com.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SchemaCheck {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:sqlite:banking.db";
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            System.out.println("Checking schema for table 'customers'...");
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(customers)");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString("name") + " Type: " + rs.getString("type"));
            }
        }
    }
}
