package com.banking.dao;

import com.banking.model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public void record(long userId, String action) {
        final String sql = "INSERT INTO audit_log (user_id, action) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, action);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to record audit log", e);
        }
    }

    public List<AuditLog> findRecent(int limit) {
        final String sql = "SELECT * FROM audit_log ORDER BY timestamp DESC LIMIT ?";
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setId(rs.getLong("id"));
                    log.setUserId(rs.getLong("user_id"));
                    log.setAction(rs.getString("action"));
                    log.setTimestamp(rs.getString("timestamp"));
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load audit logs", e);
        }
        return logs;
    }
}

