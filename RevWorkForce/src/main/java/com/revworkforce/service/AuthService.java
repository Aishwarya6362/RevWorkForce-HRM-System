package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.dao.EmployeeDAOImpl;
import com.revworkforce.util.PasswordUtil;

/**
 * AuthService handles authentication logic.
 */
public class AuthService {

    private final EmployeeDAO dao = new EmployeeDAOImpl();
    private final AuditService auditService = new AuditService();


    // ✅ CHANGE PASSWORD
    public boolean changePassword(int empId, String oldPassword, String newPassword) {

        String oldHashed = PasswordUtil.hashPassword(oldPassword);
        String newHashed = PasswordUtil.hashPassword(newPassword);

        return dao.changePassword(empId, oldHashed, newHashed);
    }
    public boolean login(int empId, String password) {

        String hashedPassword = PasswordUtil.hashPassword(password);

        if (dao.login(empId, hashedPassword)) {
            auditService.log(empId, "User logged in");
            return true;
        }
        return false;
    }
}