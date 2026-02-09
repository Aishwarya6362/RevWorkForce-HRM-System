package com.revworkforce.service;

import com.revworkforce.dao.AuditDAO;
import com.revworkforce.dao.AuditDAOImpl;

public class AuditService {

    private final AuditDAO dao = new AuditDAOImpl();

    public void log(int empId, String action) {
        dao.logAction(empId, action);
    }
}