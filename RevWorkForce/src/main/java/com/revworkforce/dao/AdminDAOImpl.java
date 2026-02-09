package com.revworkforce.dao;

import com.revworkforce.model.Employee;
import com.revworkforce.util.DBUtil;
import com.revworkforce.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** AdminDAOImpl contains all database-level operations that can be performed by ADMIN users.
 *This class handles employee creation, updates,status changes, manager mapping, and leave initialization.*/
public class AdminDAOImpl implements AdminDAO {

    private static final Logger logger =
            LogManager.getLogger(AdminDAOImpl.class);

    /* =========================================================
       1. ADD NEW EMPLOYEE
       ========================================================= */
    @Override
    public boolean addEmployee(Employee employee) {

        String sql = """
                INSERT INTO employee
                (emp_id, name, email, phone, address, dob,
                 designation, joining_date, department_id,
                 manager_id, role_id, password, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, 'ACTIVE')
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employee.getEmpId());
            ps.setString(2, employee.getName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getPhone());
            ps.setString(5, employee.getAddress());
            ps.setDate(6, employee.getDob());
            ps.setString(7, employee.getDesignation());
            ps.setInt(8, employee.getDepartmentId());

            // Manager can be null for top-level roles
            if (employee.getManagerId() == null) {
                ps.setNull(9, Types.INTEGER);
            } else {
                ps.setInt(9, employee.getManagerId());
            }

            ps.setInt(10, employee.getRoleId());

            // Password is stored only after hashing
            ps.setString(11, PasswordUtil.hashPassword(employee.getPassword()));

            boolean inserted = ps.executeUpdate() > 0;

            // Initialize default leave balance for new employee
            if (inserted) {
                initializeLeaveBalance(employee.getEmpId());
            }

            return inserted;

        } catch (Exception e) {
            logger.error("Error while adding employee empId={}",
                    employee.getEmpId(), e);
        }
        return false;
    }

    /* =========================================================
       2. UPDATE EMPLOYEE DETAILS
       ========================================================= */
    @Override
    public boolean updateEmployee(Employee e) {

        StringBuilder sql = new StringBuilder("UPDATE employee SET ");
        List<Object> params = new ArrayList<>();

        if (e.getPhone() != null) {
            sql.append("phone = ?, ");
            params.add(e.getPhone());
        }

        if (e.getAddress() != null) {
            sql.append("address = ?, ");
            params.add(e.getAddress());
        }

        if (e.getDesignation() != null) {
            sql.append("designation = ?, ");
            params.add(e.getDesignation());
        }

        if (e.getDepartmentId() > 0) {
            sql.append("department_id = ?, ");
            params.add(e.getDepartmentId());
        }

        // 🛑 Safety check (extra protection)
        if (params.isEmpty()) {
            logger.warn("No fields provided for update empId={}", e.getEmpId());
            return false;
        }

        // remove last comma + space
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE emp_id = ?");
        params.add(e.getEmpId());

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            logger.error("Error while updating employee empId={}", e.getEmpId(), ex);
        }
        return false;
    }

    /* =========================================================
       3. ACTIVATE / DEACTIVATE EMPLOYEE
       ========================================================= */
    @Override
    public boolean updateEmployeeStatus(int empId, String status) {

        String sql = "UPDATE employee SET status = ? WHERE emp_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, empId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while updating employee status empId={}",
                    empId, e);
        }
        return false;
    }

    /* =========================================================
       4. CHANGE EMPLOYEE MANAGER
       ========================================================= */
    @Override
    public boolean changeManager(int empId, Integer managerId) {

        // Prevent assigning employee as their own manager
        if (managerId != null && empId == managerId) {
            return false;
        }

        String sql = "UPDATE employee SET manager_id = ? WHERE emp_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if (managerId == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, managerId);
            }

            ps.setInt(2, empId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while changing manager empId={} managerId={}",
                    empId, managerId, e);
        }
        return false;
    }

    /* =========================================================
       5. VIEW ALL EMPLOYEES
       ========================================================= */
    @Override
    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        String sql = """
                SELECT emp_id, name, email, role_id, status
                FROM employee
                order by emp_id
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpId(rs.getInt("emp_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setRoleId(rs.getInt("role_id"));
                employee.setStatus(rs.getString("status"));
                employees.add(employee);
            }

        } catch (Exception e) {
            logger.error("Error while fetching employee list", e);
        }
        return employees;
    }

    /* =========================================================
       6. SEARCH EMPLOYEES
       ========================================================= */
    @Override
    public List<Employee> searchEmployees(String keyword) {

        List<Employee> employees = new ArrayList<>();

        String sql = """
                SELECT emp_id, name, email, designation, status
                FROM employee
                WHERE LOWER(name) LIKE ?
                   OR LOWER(designation) LIKE ?
                   OR emp_id = ?
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword.toLowerCase() + "%");

            // Handle numeric search safely
            try {
                ps.setInt(3, Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                ps.setInt(3, -1);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpId(rs.getInt("emp_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setDesignation(rs.getString("designation"));
                employee.setStatus(rs.getString("status"));
                employees.add(employee);
            }

        } catch (Exception e) {
            logger.error("Error while searching employees keyword={}",
                    keyword, e);
        }
        return employees;
    }

    /* =========================================================
       7. INITIALIZE LEAVE BALANCE FOR NEW EMPLOYEE
       ========================================================= */
    @Override
    public boolean initializeLeaveBalance(int empId) {

        String sql = """
                INSERT INTO leave_balance (emp_id, leave_type_id, balance)
                SELECT ?, leave_type_id, max_days_per_year
                FROM leave_type
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while initializing leave balance empId={}",
                    empId, e);
        }
        return false;
    }
}