package com.revworkforce.service;

import com.revworkforce.dao.AdminDAO;
import com.revworkforce.dao.AdminDAOImpl;
import com.revworkforce.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * AdminService contains business logic related to
 * employee management performed by ADMIN users.
 */
public class AdminService {

    private final AdminDAO dao = new AdminDAOImpl();
    private static final Logger logger =
            LogManager.getLogger(AdminService.class);

    // ===== EMPLOYEE MANAGEMENT =====

    public boolean addEmployee(Employee emp) {
        logger.info("Admin adding employee empId={}", emp.getEmpId());
        return dao.addEmployee(emp);
    }

    public boolean updateEmployee(Employee emp) {
        logger.info("Admin updating employee empId={}", emp.getEmpId());
        return dao.updateEmployee(emp);
    }

    public boolean updateEmployeeStatus(int empId, String status) {
        logger.info("Updating employee status empId={} status={}", empId, status);
        return dao.updateEmployeeStatus(empId, status);
    }

    public boolean changeManager(int empId, Integer managerId) {
        logger.info("Changing manager empId={} managerId={}", empId, managerId);
        return dao.changeManager(empId, managerId);
    }

    // ===== VIEW OPERATIONS =====

    public List<Employee> getAllEmployees() {
        return dao.getAllEmployees();
    }

    public List<Employee> searchEmployees(String keyword) {
        return dao.searchEmployees(keyword);
    }
}