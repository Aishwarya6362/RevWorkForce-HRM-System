package com.revworkforce.model;

import java.sql.Date;

/**
 * LeaveRequest model represents employee leave data.
 *
 * This class is reused for multiple purposes:
 * - Viewing leave balance
 * - Applying leave
 * - Viewing personal leave history
 * - Manager reviewing team leaves
 *
 * Only relevant fields are populated based on the use case.
 */
public class LeaveRequest {

    // ===== IDENTIFIERS =====
    private int leaveRequestId;
    private int empId;
    private int leaveTypeId;

    // ===== LEAVE DETAILS =====
    private Date startDate;
    private Date endDate;
    private String reason;
    private String status;

    // ===== DISPLAY / VIEW PURPOSE FIELDS =====
    private String leaveName;      // Used while showing leave type name
    private int balance;           // Used while showing leave balance
    private String employeeName;   // Used in manager view

    // ===== GETTERS & SETTERS =====

    public int getLeaveRequestId() {
        return leaveRequestId;
    }
    public void setLeaveRequestId(int leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }
    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveName() {
        return leaveName;
    }
    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}