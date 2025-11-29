package com.banking.dao;

import com.banking.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void record(Transaction transaction) {
        final String sql = """
                INSERT INTO transactions (account_id, type, amount, description)
                VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, transaction.getAccountId());
            stmt.setString(2, transaction.getType());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getDescription());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    transaction.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to record transaction", e);
        }
    }

    public List<Transaction> findByAccount(long accountId) {
        final String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                    transactions.add(map(rs));
            }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load transactions", e);
        }
        return transactions;
    }

    public List<Transaction> findByCustomer(long customerId) {
        final String sql = """
                SELECT t.*
                FROM transactions t
                JOIN accounts a ON t.account_id = a.id
            WHERE a.customer_id = ? 
            ORDER BY t.timestamp DESC
            """;
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                    transactions.add(map(rs));
            }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load customer transactions", e);
        }
        return transactions;
    }

    private Transaction map(ResultSet rs) throws SQLException {
        Transaction tx = new Transaction();
        tx.setId(rs.getLong("id"));
        tx.setAccountId(rs.getLong("account_id"));
        tx.setType(rs.getString("type"));
        tx.setAmount(rs.getBigDecimal("amount"));
        tx.setDescription(rs.getString("description"));
        tx.setTimestamp(rs.getString("timestamp"));
        return tx;
    }
}
