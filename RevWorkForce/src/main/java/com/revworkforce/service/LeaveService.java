package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.LeaveRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.temporal.ChronoUnit;
import java.util.List;


/**
 * LeaveService handles leave-related business logic.
 */
public class LeaveService {

    private final LeaveDAO dao = new LeaveDAOImpl();
    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private final NotificationService notificationService =
            new NotificationService();
    private final AuditService auditService =
            new AuditService();

    private static final Logger logger =
            LogManager.getLogger(LeaveService.class);

    // ===== VIEW LEAVE BALANCE =====
    public List<LeaveRequest> viewBalance(int empId) {
        logger.info("Fetching leave balance empId={}", empId);
        return dao.getLeaveBalance(empId);
    }


    // ===== APPLY LEAVE =====
    public boolean applyLeave(LeaveRequest lr) {

        logger.info("Leave apply request empId={} typeId={}",
                lr.getEmpId(), lr.getLeaveTypeId());
// Ensure leave balance exists (for old employees)
        dao.initializeLeaveBalanceIfMissing(lr.getEmpId());

        int days = (int) ChronoUnit.DAYS.between(
                lr.getStartDate().toLocalDate(),
                lr.getEndDate().toLocalDate()
        ) + 1;

        int balance = dao.getLeaveBalance(
                lr.getEmpId(), lr.getLeaveTypeId());

        if (days > balance) {
            logger.warn("Insufficient leave balance empId={}", lr.getEmpId());
            return false;
        }

        if (dao.hasOverlappingLeave(
                lr.getEmpId(),
                lr.getStartDate(),
                lr.getEndDate())) {

            logger.warn("Overlapping leave empId={}", lr.getEmpId());
            return false;
        }

        boolean applied = dao.applyLeave(lr);

        if (applied) {

            Integer managerId = employeeDAO.getManagerId(lr.getEmpId());

            if (managerId == null) {
                // ✅ Auto-approved → reduce balance immediately
                dao.reduceLeaveBalance(
                        lr.getEmpId(),
                        lr.getLeaveTypeId(),
                        days
                );
            } else {
                // 🔔 Normal employee → notify manager
                notificationService.notify(
                        managerId,
                        "New leave request from Employee ID " + lr.getEmpId()
                );
            }

            // 🧾 AUDIT LOG
            auditService.log(lr.getEmpId(), "Leave applied");
        }

        return applied;

    }

    // ===== VIEW MY LEAVES =====
    public List<LeaveRequest> viewMyLeaves(int empId) {
        return dao.getMyLeaves(empId);
    }

    // ===== VIEW TEAM LEAVES =====
    public List<LeaveRequest> viewTeamLeaves(int managerId) {
        return dao.getTeamLeaveRequests(managerId);
    }

    // ===== APPROVE / REJECT =====
    // ===== APPROVE / REJECT =====
    public boolean updateLeaveStatus(int id, String status, String comment) {

        LeaveRequest lr = dao.getLeaveRequestById(id);

        if (lr == null || !"PENDING".equalsIgnoreCase(lr.getStatus())) {
            return false;
        }

        boolean updated = dao.updateLeaveStatus(id, status, comment);

        if (!updated) {
            return false;
        }

        if ("APPROVED".equalsIgnoreCase(status)) {

            int days = (int) ChronoUnit.DAYS.between(
                    lr.getStartDate().toLocalDate(),
                    lr.getEndDate().toLocalDate()
            ) + 1;

            boolean reduced = dao.reduceLeaveBalance(
                    lr.getEmpId(),
                    lr.getLeaveTypeId(),
                    days
            );

            if (!reduced) {
                dao.updateLeaveStatus(
                        id,
                        "PENDING",
                        "Rollback: leave balance update failed"
                );
                return false;
            }
        }

        // 🔔 Notify employee (THIS IS WHY EMPLOYEE SHOULD SEE IT)
        notificationService.notify(
                lr.getEmpId(),
                "Your leave request (ID: " + id + ") was " + status
        );

        // 🧾 Audit manager action
        Integer managerId = employeeDAO.getManagerId(lr.getEmpId());
        if (managerId != null) {
            auditService.log(
                    managerId,
                    "Leave " + status + " for leaveId=" + id
            );
        }

        return true;
    }


    // ===== CANCEL LEAVE =====
    // ===== CANCEL LEAVE =====
    public boolean cancelLeave(int leaveRequestId, int empId) {

        boolean cancelled =
                dao.cancelPendingLeave(leaveRequestId, empId);

        if (cancelled) {

            // 🧾 Audit log
            auditService.log(
                    empId,
                    "Leave cancelled (ID=" + leaveRequestId + ")"
            );

            // 🔔 Notify manager (if employee has one)
            Integer managerId = employeeDAO.getManagerId(empId);
            if (managerId != null) {
                notificationService.notify(
                        managerId,
                        "Leave request (ID: " + leaveRequestId + ") was cancelled"
                );
            }

            // 🔔 Notify employee (confirmation)
            notificationService.notify(
                    empId,
                    "Your leave request (ID: " + leaveRequestId + ") has been cancelled"
            );
        }

        return cancelled;
    }


}
