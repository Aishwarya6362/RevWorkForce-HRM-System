package com.revworkforce.dao;

import com.revworkforce.model.Announcement;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAOImpl implements AnnouncementDAO {

    private static final Logger logger =
            LogManager.getLogger(AnnouncementDAOImpl.class);

    @Override
    public boolean addAnnouncement(Announcement a) {

        String sql = """
        INSERT INTO announcements
        (announcement_id, title, message, created_by, created_date)
        VALUES (announcement_seq.NEXTVAL, ?, ?, ?, SYSDATE)
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());
            ps.setInt(3, a.getCreatedBy());

            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            logger.error("Error adding announcement", e);
        }
        return false;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {

        List<Announcement> list = new ArrayList<>();

        String sql = """
                SELECT announcement_id, title, message, created_by, created_date
                FROM announcements
                ORDER BY created_date DESC
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Announcement a = new Announcement();
                a.setAnnouncementId(rs.getInt("announcement_id"));
                a.setTitle(rs.getString("title"));
                a.setMessage(rs.getString("message"));
                a.setCreatedBy(rs.getInt("created_by"));
                a.setCreatedDate(rs.getDate("created_date"));
                list.add(a);
            }

        } catch (Exception e) {
            logger.error("Error fetching announcements", e);
        }
        return list;
    }
}