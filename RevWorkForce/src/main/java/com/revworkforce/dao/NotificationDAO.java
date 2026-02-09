package com.revworkforce.dao;

import com.revworkforce.model.Notification;
import java.util.List;

/**
 * NotificationDAO defines database operations
 * related to employee notifications.
 */
public interface NotificationDAO {

    // ================= CREATE =================

    boolean addNotification(int empId, String message);

    // ================= READ =================

    List<Notification> getUnreadNotifications(int empId);

    // ================= UPDATE =================

    void markAsRead(int empId);
}