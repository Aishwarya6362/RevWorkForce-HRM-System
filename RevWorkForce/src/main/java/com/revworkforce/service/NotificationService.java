package com.revworkforce.service;

import com.revworkforce.dao.NotificationDAO;
import com.revworkforce.dao.NotificationDAOImpl;
import com.revworkforce.model.Notification;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * NotificationService handles employee notifications.
 */
public class NotificationService {

    private static final Logger logger =
            LogManager.getLogger(NotificationService.class);

    private final NotificationDAO dao = new NotificationDAOImpl();

    public List<Notification> getUnreadNotifications(int empId) {
        logger.info("Fetching unread notifications empId={}", empId);
        return dao.getUnreadNotifications(empId);
    }

    public void markAsRead(int empId) {
        dao.markAsRead(empId);
        logger.info("Notifications marked READ empId={}", empId);
    }

    public void notify(int empId, String message) {
        dao.addNotification(empId, message);
        logger.info("Notification sent empId={}", empId);
    }

    public void notifyAllEmployees(String message) {

        String sql = """
            INSERT INTO notification (notification_id, emp_id, message)
            SELECT notification_seq.NEXTVAL, emp_id, ?
            FROM employee
            WHERE status = 'ACTIVE'
            """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Error creating announcement notifications", e);
        }
    }
}