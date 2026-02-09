package com.revworkforce.model;

import java.sql.Date;

/**
 * Notification model represents a system-generated message
 * sent to an employee.
 *
 * Examples:
 * - Leave approved / rejected
 * - Performance review feedback added
 *
 * This model is mainly used for display purposes.
 */
public class Notification {

    // ===== NOTIFICATION CONTENT =====
    private String message;

    // Date when the notification was created
    private Date createdDate;

    // ===== GETTERS & SETTERS =====

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}