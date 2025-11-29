package com.banking.dao;

import com.banking.model.Account;
import com.banking.model.AccountFactory;
import com.banking.model.AccountType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO {

    public Account save(Account account) {
        final String sql = """
                INSERT INTO accounts (account_number, customer_id, type, balance, branch)
                VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setLong(2, account.getCustomerId());
            stmt.setString(3, account.getType().name());
            stmt.setBigDecimal(4, account.getBalance());
            stmt.setString(5, account.getBranch());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    account.setId(keys.getLong(1));
                }
            }
            return account;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save account", e);
        }
    }

    public List<Account> findByCustomerId(long customerId) {
        final String sql = "SELECT * FROM accounts WHERE customer_id = ? ORDER BY id DESC";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                    accounts.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load accounts", e);
        }
        return accounts;
    }

    public List<Account> findAll() {
        final String sql = "SELECT * FROM accounts ORDER BY id DESC";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                accounts.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load accounts", e);
        }
        return accounts;
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        final String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find account", e);
        }
        return Optional.empty();
    }

    public void updateBalance(long accountId, BigDecimal newBalance) {
        final String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update balance", e);
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        AccountType type = AccountType.fromDb(rs.getString("type"));
        Account account = AccountFactory.rehydrate(type);
        account.setId(rs.getLong("id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setCustomerId(rs.getLong("customer_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setBranch(rs.getString("branch"));
        return account;
    }
}
