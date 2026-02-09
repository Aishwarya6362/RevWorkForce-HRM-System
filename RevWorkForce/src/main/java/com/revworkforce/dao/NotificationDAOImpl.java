package com.revworkforce.dao;

import com.revworkforce.model.Notification;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationDAOImpl handles database operations
 * for employee notifications.
 */
public class NotificationDAOImpl implements NotificationDAO {

    private static final Logger logger =
            LogManager.getLogger(NotificationDAOImpl.class);

    // ================= CREATE =================

    @Override
    public boolean addNotification(int empId, String message) {

        String sql = """
                INSERT INTO notification
                (notification_id, emp_id, message, status, created_date)
                VALUES (notification_seq.NEXTVAL, ?, ?, 'UNREAD', SYSDATE)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, message);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error(
                    "Error while adding notification empId={} message={}",
                    empId, message, e
            );
        }
        return false;
    }

    // ================= READ =================

    @Override
    public List<Notification> getUnreadNotifications(int empId) {

        List<Notification> notifications = new ArrayList<>();

        String sql = """
                SELECT message, created_date
                FROM notification
                WHERE emp_id = ?
                  AND status = 'UNREAD'
                ORDER BY created_date DESC
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setMessage(rs.getString("message"));
                n.setCreatedDate(rs.getDate("created_date"));
                notifications.add(n);
            }

        } catch (Exception e) {
            logger.error(
                    "Error while fetching unread notifications empId={}",
                    empId, e
            );
        }
        return notifications;
    }

    // ================= UPDATE =================

    @Override
    public void markAsRead(int empId) {

        String sql = """
                UPDATE notification
                SET status = 'READ'
                WHERE emp_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error(
                    "Error while marking notifications as read empId={}",
                    empId, e
            );
        }
    }
}