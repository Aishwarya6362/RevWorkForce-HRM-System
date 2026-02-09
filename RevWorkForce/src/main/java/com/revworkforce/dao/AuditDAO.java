package com.revworkforce.dao;

public interface AuditDAO {
    void logAction(int empId, String action);
}