package com.banking.service;

import com.banking.dao.AuditLogDAO;
import com.banking.model.AuditLog;

import java.util.List;

public class AuditService {

    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    public void record(long userId, String action) {
        auditLogDAO.record(userId, action);
    }

    public List<AuditLog> recentLogs(int limit) {
        return auditLogDAO.findRecent(limit);
    }
}

