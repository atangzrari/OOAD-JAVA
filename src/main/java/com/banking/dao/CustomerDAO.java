package com.banking.dao;

import com.banking.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO {

    public Customer save(Customer customer) {
        final String sql = """
                    INSERT INTO customers (user_id, first_name, last_name, address, employed, employer_name, employer_address)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, customer.getUserId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5, customer.isEmployed() ? 1 : 0);
            stmt.setString(6, customer.getEmployerName());
            stmt.setString(7, customer.getEmployerAddress());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    customer.setId(keys.getLong(1));
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save customer", e);
        }
    }

    public void update(Customer customer) {
        final String sql = """
                    UPDATE customers
                    SET first_name = ?, last_name = ?, address = ?, employed = ?, employer_name = ?, employer_address = ?
                    WHERE id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getAddress());
            stmt.setInt(4, customer.isEmployed() ? 1 : 0);
            stmt.setString(5, customer.getEmployerName());
            stmt.setString(6, customer.getEmployerAddress());
            stmt.setLong(7, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update customer", e);
        }
    }

    public Optional<Customer> findById(long id) {
        final String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find customer", e);
        }
        return Optional.empty();
    }

    public Optional<Customer> findByUserId(long userId) {
        final String sql = "SELECT * FROM customers WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load customer by user id", e);
        }
        return Optional.empty();
    }

    public List<Customer> findAll() {
        final String sql = "SELECT * FROM customers ORDER BY last_name";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(map(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list customers", e);
        }
        return customers;
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setUserId(rs.getLong("user_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setAddress(rs.getString("address"));
        customer.setEmployed(rs.getInt("employed") == 1);
        customer.setEmployerName(rs.getString("employer_name"));
        customer.setEmployerAddress(rs.getString("employer_address"));
        return customer;
    }
}
