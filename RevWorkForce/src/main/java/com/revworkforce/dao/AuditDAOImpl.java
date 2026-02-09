package com.revworkforce.dao;

import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AuditDAOImpl implements AuditDAO {

    private static final Logger logger =
            LogManager.getLogger(AuditDAOImpl.class);

    @Override
    public void logAction(int empId, String action) {

        String sql = """
            INSERT INTO audit_log
            (log_id, emp_id, action, action_date)
            VALUES (audit_seq.NEXTVAL, ?, ?, SYSDATE)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, action);
            ps.executeUpdate();

        } catch (Exception e) {
            // 🔴 technical error → log4j
            logger.error("Error inserting audit log", e);
        }
    }
}