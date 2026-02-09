package com.revworkforce.dao;

import com.revworkforce.model.LeaveRequest;

import java.sql.Date;
import java.util.List;

/**
 * LeaveDAO defines database operations
 * related to employee leave management.
 */
public interface LeaveDAO {

    // ================= READ =================

    List<LeaveRequest> getLeaveBalance(int empId);

    List<LeaveRequest> getMyLeaves(int empId);

    List<LeaveRequest> getTeamLeaveRequests(int managerId);

    LeaveRequest getLeaveRequestById(int leaveRequestId);

    int getLeaveBalance(int empId, int leaveTypeId);

    // ================= VALIDATION =================

    boolean hasOverlappingLeave(int empId, Date start, Date end);

    // ================= CREATE =================

    boolean applyLeave(LeaveRequest leave);

    // ================= UPDATE =================

    boolean cancelPendingLeave(int leaveRequestId, int empId);

    boolean updateLeaveStatus(int id, String status, String comment);

    boolean reduceLeaveBalance(int empId, int leaveTypeId, int days);
    boolean initializeLeaveBalanceIfMissing(int empId);

    boolean restoreLeaveBalance(int empId, int leaveTypeId, int days);
}