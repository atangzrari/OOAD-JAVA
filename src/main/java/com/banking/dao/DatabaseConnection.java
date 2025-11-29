package com.banking.dao;

import com.banking.util.PasswordHasher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:sqlite:banking_fixed.db";
    private static Connection connection;

    private DatabaseConnection() {
    }

    private static String resolveUrl() {
        return System.getProperty("banking.db.url", DEFAULT_URL);
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(resolveUrl());
                try (Statement pragma = connection.createStatement()) {
                    pragma.execute("PRAGMA foreign_keys = ON");
                }
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found", e);
            }
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection ignored = getConnection()) {
            createSchema();
            seedAdminUser();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to initialize database", e);
        }
    }

    private static void createSchema() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        role TEXT NOT NULL CHECK(role IN ('ADMIN','CUSTOMER'))
                    )
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS customers (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER UNIQUE,
                        first_name TEXT NOT NULL,
                        last_name TEXT NOT NULL,
                        address TEXT,
                        employed INTEGER NOT NULL DEFAULT 0,
                        employer_name TEXT,
                        employer_address TEXT,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                    )
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS accounts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        account_number TEXT NOT NULL UNIQUE,
                        customer_id INTEGER NOT NULL,
                        type TEXT NOT NULL CHECK(type IN ('SAVINGS','INVESTMENT','CHEQUE')),
                        balance NUMERIC NOT NULL DEFAULT 0,
                        branch TEXT,
                        FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
                    )
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        account_id INTEGER NOT NULL,
                        type TEXT NOT NULL CHECK(type IN ('DEPOSIT','WITHDRAWAL','INTEREST')),
                        amount NUMERIC NOT NULL,
                        description TEXT,
                        timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
                    )
                    """);
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS audit_log (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        user_id INTEGER,
                        action TEXT NOT NULL,
                        timestamp TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
                    )
                    """);
        }
    }

    private static void seedAdminUser() throws SQLException {
        final String seedSql = """
                INSERT INTO users (username, password_hash, role)
                SELECT 'admin', ?, 'ADMIN'
                WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin')
                """;
        try (var pstmt = getConnection().prepareStatement(seedSql)) {
            pstmt.setString(1, PasswordHasher.hash("admin123"));
            pstmt.executeUpdate();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to close database connection", e);
        }
    }
}