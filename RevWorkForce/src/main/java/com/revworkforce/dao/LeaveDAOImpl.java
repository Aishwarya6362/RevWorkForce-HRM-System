package com.revworkforce.dao;

import com.revworkforce.model.LeaveRequest;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAOImpl implements LeaveDAO {

    private static final Logger logger =
            LogManager.getLogger(LeaveDAOImpl.class);

    // ================= READ =================

    @Override
    public List<LeaveRequest> getLeaveBalance(int empId) {

        List<LeaveRequest> list = new ArrayList<>();

        String sql = """
    SELECT lt.leave_type_id, lt.leave_name, lb.balance
    FROM leave_balance lb
    JOIN leave_type lt ON lb.leave_type_id = lt.leave_type_id
    WHERE lb.emp_id = ?
    ORDER BY lt.leave_type_id
    """;


        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setLeaveName(rs.getString("leave_name"));
                lr.setBalance(rs.getInt("balance"));
                list.add(lr);
            }

        } catch (Exception e) {
            logger.error("Error while fetching leave balance empId={}", empId, e);
        }
        return list;
    }

    @Override
    public List<LeaveRequest> getMyLeaves(int empId) {

        List<LeaveRequest> list = new ArrayList<>();

        String sql = """
                SELECT lr.leave_request_id, lt.leave_name,
                       lr.start_date, lr.end_date, lr.status
                FROM leave_request lr
                JOIN leave_type lt ON lr.leave_type_id = lt.leave_type_id
                WHERE lr.emp_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setLeaveRequestId(rs.getInt("leave_request_id"));
                lr.setLeaveName(rs.getString("leave_name"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setStatus(rs.getString("status"));
                list.add(lr);
            }

        } catch (Exception e) {
            logger.error("Error while fetching my leaves empId={}", empId, e);
        }
        return list;
    }

    @Override
    public List<LeaveRequest> getTeamLeaveRequests(int managerId) {

        List<LeaveRequest> list = new ArrayList<>();

        String sql = """
                SELECT lr.leave_request_id, e.name, lt.leave_name,
                       lr.start_date, lr.end_date, lr.status
                FROM leave_request lr
                JOIN employee e ON lr.emp_id = e.emp_id
                JOIN leave_type lt ON lr.leave_type_id = lt.leave_type_id
                WHERE e.manager_id = ?
                ORDER BY lr.leave_request_id
                
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setLeaveRequestId(rs.getInt("leave_request_id"));
                lr.setEmployeeName(rs.getString("name"));
                lr.setLeaveName(rs.getString("leave_name"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setStatus(rs.getString("status"));
                list.add(lr);
            }

        } catch (Exception e) {
            logger.error("Error while fetching team leaves managerId={}", managerId, e);
        }
        return list;
    }

    @Override
    public LeaveRequest getLeaveRequestById(int leaveRequestId) {

        String sql = """
                SELECT emp_id, leave_type_id, start_date, end_date, status
                FROM leave_request
                WHERE leave_request_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveRequestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                LeaveRequest lr = new LeaveRequest();
                lr.setEmpId(rs.getInt("emp_id"));
                lr.setLeaveTypeId(rs.getInt("leave_type_id"));
                lr.setStartDate(rs.getDate("start_date"));
                lr.setEndDate(rs.getDate("end_date"));
                lr.setStatus(rs.getString("status"));
                return lr;
            }

        } catch (Exception e) {
            logger.error("Error while fetching leave request requestId={}",
                    leaveRequestId, e);
        }
        return null;
    }

    @Override
    public int getLeaveBalance(int empId, int leaveTypeId) {

        String sql = """
                SELECT balance
                FROM leave_balance
                WHERE emp_id = ? AND leave_type_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, leaveTypeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("balance");
            }

        } catch (Exception e) {
            logger.error("Error while fetching leave balance empId={} leaveTypeId={}",
                    empId, leaveTypeId, e);
        }
        return 0;
    }

    // ================= VALIDATION =================

    @Override
    public boolean hasOverlappingLeave(int empId, Date start, Date end) {

        String sql = """
                SELECT COUNT(*)
                FROM leave_request
                WHERE emp_id = ?
                  AND status IN ('PENDING', 'APPROVED')
                  AND (start_date <= ? AND end_date >= ?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setDate(2, end);
            ps.setDate(3, start);

            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (Exception e) {
            logger.error("Error while checking overlapping leave empId={}", empId, e);
        }
        return false;
    }

    // ================= CREATE =================

    /**
     * ✅ FIXED: Apply leave with proper validation and balance update
     * (No methods removed, only logic added)
     */
    @Override
    public boolean applyLeave(LeaveRequest leave) {

        String insertSql = """
                INSERT INTO leave_request
                (leave_request_id, emp_id, leave_type_id,
                 start_date, end_date, reason, status)
                VALUES (leave_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBUtil.getConnection()) {

            con.setAutoCommit(false);

            // 1️⃣ Ensure leave balance exists
            initializeLeaveBalanceIfMissing(leave.getEmpId());

            // 2️⃣ Overlap check
            if (hasOverlappingLeave(
                    leave.getEmpId(),
                    leave.getStartDate(),
                    leave.getEndDate())) {
                con.rollback();
                return false;
            }

            // 3️⃣ Calculate days (inclusive)
            long days = ChronoUnit.DAYS.between(
                    leave.getStartDate().toLocalDate(),
                    leave.getEndDate().toLocalDate()) + 1;

            if (days <= 0) {
                con.rollback();
                return false;
            }

            // 4️⃣ Reduce leave balance
            boolean reduced = reduceLeaveBalance(
                    leave.getEmpId(),
                    leave.getLeaveTypeId(),
                    (int) days);

            if (!reduced) {
                con.rollback();
                return false;
            }

            // 5️⃣ Insert leave request
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {

                ps.setInt(1, leave.getEmpId());
                ps.setInt(2, leave.getLeaveTypeId());
                ps.setDate(3, leave.getStartDate());
                ps.setDate(4, leave.getEndDate());
                ps.setString(5, leave.getReason());

                String status =
                        hasManager(leave.getEmpId())
                                ? "PENDING"
                                : "APPROVED";

                ps.setString(6, status);

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            logger.error("Error while applying leave empId={}",
                    leave.getEmpId(), e);
        }
        return false;
    }

    // ================= UPDATE =================

    @Override
    public boolean cancelPendingLeave(int leaveRequestId, int empId) {

        String sql = """
                UPDATE leave_request
                SET status = 'CANCELLED'
                WHERE leave_request_id = ?
                  AND emp_id = ?
                  AND status = 'PENDING'
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveRequestId);
            ps.setInt(2, empId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while cancelling leave requestId={} empId={}",
                    leaveRequestId, empId, e);
        }
        return false;
    }

    @Override
    public boolean updateLeaveStatus(int id, String status, String comment) {

        String sql = """
                UPDATE leave_request
                SET status = ?, manager_comments = ?
                WHERE leave_request_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while updating leave status requestId={} status={}",
                    id, status, e);
        }
        return false;
    }

    // ================= BALANCE =================

    @Override
    public boolean reduceLeaveBalance(int empId, int leaveTypeId, int days) {

        String sql = """
                UPDATE leave_balance
                SET balance = balance - ?
                WHERE emp_id = ?
                  AND leave_type_id = ?
                  AND balance >= ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.setInt(3, leaveTypeId);
            ps.setInt(4, days);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while reducing leave balance empId={} leaveTypeId={}",
                    empId, leaveTypeId, e);
        }
        return false;
    }

    @Override
    public boolean restoreLeaveBalance(int empId, int leaveTypeId, int days) {

        String sql = """
                UPDATE leave_balance
                SET balance = balance + ?
                WHERE emp_id = ?
                  AND leave_type_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.setInt(3, leaveTypeId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while restoring leave balance empId={}", empId, e);
        }
        return false;
    }

    @Override
    public boolean initializeLeaveBalanceIfMissing(int empId) {

        String sql = """
                INSERT INTO leave_balance (emp_id, leave_type_id, balance)
                SELECT ?, lt.leave_type_id, lt.max_days_per_year
                FROM leave_type lt
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM leave_balance lb
                    WHERE lb.emp_id = ?
                      AND lb.leave_type_id = lt.leave_type_id
                )
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, empId);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            logger.error("Error initializing leave balance empId={}", empId, e);
        }
        return false;
    }

    // ================= HELPER =================

    private boolean hasManager(int empId) {

        String sql = "SELECT manager_id FROM employee WHERE emp_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                rs.getInt("manager_id");
                return !rs.wasNull();
            }

        } catch (Exception e) {
            logger.error("Error checking manager for empId={}", empId, e);
        }
        return false;
    }
}
