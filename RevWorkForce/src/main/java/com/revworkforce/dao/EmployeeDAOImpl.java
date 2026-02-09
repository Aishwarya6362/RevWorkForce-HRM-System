package com.revworkforce.dao;

import com.revworkforce.model.EmployeeProfile;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAOImpl handles database operations related to
 * employee login and profile information.
 *
 * These operations are common across all roles.
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    private static final Logger logger =
            LogManager.getLogger(EmployeeDAOImpl.class);

    /* =========================================================
       1. LOGIN VALIDATION
       ========================================================= */
    @Override
    public boolean login(int empId, String password) {

        String sql = """
                SELECT 1
                FROM employee
                WHERE emp_id = ?
                  AND password = ?
                  AND status = 'ACTIVE'
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, password);

            return ps.executeQuery().next();

        } catch (Exception e) {
            logger.error("Error during login validation empId={}", empId, e);
        }
        return false;
    }

    /* =========================================================
       2. VIEW EMPLOYEE PROFILE
       ========================================================= */
    @Override
    public EmployeeProfile viewProfile(int empId) {

        EmployeeProfile profile = null;

        String sql = """
                SELECT e.emp_id,
                       e.name,
                       e.email,
                       e.phone,
                       e.address,
                       e.emergency_contact,
                       r.role_name,
                       d.department_name,
                       m.emp_id   AS manager_id,
                       m.name     AS manager_name
                FROM employee e
                JOIN role r ON e.role_id = r.role_id
                JOIN department d ON e.department_id = d.department_id
                LEFT JOIN employee m ON e.manager_id = m.emp_id
                WHERE e.emp_id = ?
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                profile = new EmployeeProfile();
                profile.setEmpId(rs.getInt("emp_id"));
                profile.setName(rs.getString("name"));
                profile.setEmail(rs.getString("email"));
                profile.setPhone(rs.getString("phone"));
                profile.setAddress(rs.getString("address"));
                profile.setEmergencyContact(rs.getString("emergency_contact"));
                profile.setRoleName(rs.getString("role_name"));
                profile.setDepartmentName(rs.getString("department_name"));
                profile.setManagerId(rs.getInt("manager_id"));
                profile.setManagerName(rs.getString("manager_name"));
            }

        } catch (Exception e) {
            logger.error("Error while fetching employee profile empId={}", empId, e);
        }
        return profile;
    }

    /* =========================================================
       3. FETCH MANAGER ID
       ========================================================= */
    @Override
    public Integer getManagerId(int empId) {

        String sql = "SELECT manager_id FROM employee WHERE emp_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int managerId = rs.getInt("manager_id");
                return rs.wasNull() ? null : managerId;
            }

        } catch (Exception e) {
            logger.error("Error while fetching managerId empId={}", empId, e);
        }
        return null;
    }

    /* =========================================================
       4. UPDATE EMPLOYEE PROFILE (PHONE / ADDRESS / EMERGENCY)
       ========================================================= */
    @Override
    public boolean updateProfile(int empId,
                                 String phone,
                                 String address,
                                 String emergencyContact) {

        String sql = """
                UPDATE employee
                SET phone = ?,
                    address = ?,
                    emergency_contact = ?
                WHERE emp_id = ?
                  AND status = 'ACTIVE'
                """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setString(3, emergencyContact);
            ps.setInt(4, empId);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            logger.error("Error updating profile empId={}", empId, e);
        }
        return false;
    }
    @Override
    public boolean changePassword(int empId, String oldHashedPassword, String newHashedPassword) {

        String sql = """
        UPDATE employee
        SET password = ?
        WHERE emp_id = ?
          AND password = ?
          AND status = 'ACTIVE'
        """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, newHashedPassword);
            ps.setInt(2, empId);
            ps.setString(3, oldHashedPassword);

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            logger.error("Error changing password empId={}", empId, e);
        }
        return false;
    }
    // ================= UPCOMING BIRTHDAYS =================
    @Override
    public List<EmployeeProfile> getUpcomingBirthdays() {

        List<EmployeeProfile> list = new ArrayList<>();

        String sql = """
                SELECT emp_id, name, dob
                FROM employee
                WHERE status='ACTIVE'
                AND TO_CHAR(dob,'MMDD')
                BETWEEN TO_CHAR(SYSDATE,'MMDD')
                AND TO_CHAR(SYSDATE+7,'MMDD')
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EmployeeProfile e = new EmployeeProfile();
                e.setEmpId(rs.getInt("emp_id"));
                e.setName(rs.getString("name"));
                e.setDob(rs.getDate("dob"));
                list.add(e);
            }


        } catch (Exception e) {
            logger.error("Birthday fetch error", e);
        }
        return list;
    }

    // ================= WORK ANNIVERSARIES (FIXED) =================
    @Override
    public List<EmployeeProfile> getWorkAnniversaries() {

        List<EmployeeProfile> list = new ArrayList<>();

        String sql = """
                SELECT emp_id,
                       name,
                       FLOOR(MONTHS_BETWEEN(SYSDATE, joining_date) / 12) AS years_completed
                FROM employee
                WHERE status = 'ACTIVE'
                AND TO_CHAR(joining_date,'MM') = TO_CHAR(SYSDATE,'MM')
                AND FLOOR(MONTHS_BETWEEN(SYSDATE, joining_date) / 12) > 0
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EmployeeProfile e = new EmployeeProfile();
                e.setEmpId(rs.getInt("emp_id"));
                e.setName(rs.getString("name"));
                e.setYearsCompleted(rs.getInt("years_completed"));
                list.add(e);
            }

        } catch (Exception e) {
            logger.error("Anniversary fetch error", e);
        }
        return list;
    }

}
