package com.revworkforce.dao;

import com.revworkforce.model.Holiday;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * HolidayDAOImpl contains database logic
 * for managing company holidays.
 */
public class HolidayDAOImpl implements HolidayDAO {

    private static final Logger logger =
            LogManager.getLogger(HolidayDAOImpl.class);

    // ================= READ =================

    @Override
    public List<Holiday> getAllHolidays() {

        List<Holiday> holidays = new ArrayList<>();

        String sql = """
                SELECT holiday_id, holiday_name, holiday_date, description
                FROM holidays
                ORDER BY holiday_date
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Holiday h = new Holiday();
                h.setHolidayId(rs.getInt("holiday_id"));
                h.setHolidayName(rs.getString("holiday_name"));
                h.setHolidayDate(rs.getDate("holiday_date"));
                h.setDescription(rs.getString("description"));
                holidays.add(h);
            }

        } catch (Exception e) {
            logger.error("Error while fetching holidays", e);
        }
        return holidays;
    }

    // ================= CREATE =================

    @Override
    public boolean addHoliday(Holiday holiday) {

        String sql = """
                INSERT INTO holidays
                VALUES (holiday_seq.NEXTVAL, ?, ?, ?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, holiday.getHolidayName());
            ps.setDate(2, holiday.getHolidayDate());
            ps.setString(3, holiday.getDescription());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while adding holiday name={}",
                    holiday.getHolidayName(), e);
        }
        return false;
    }

    // ================= UPDATE =================

    @Override
    public boolean updateHoliday(Holiday holiday) {

        String sql = """
                UPDATE holidays
                SET holiday_name = ?, holiday_date = ?, description = ?
                WHERE holiday_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, holiday.getHolidayName());
            ps.setDate(2, holiday.getHolidayDate());
            ps.setString(3, holiday.getDescription());
            ps.setInt(4, holiday.getHolidayId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while updating holiday id={}",
                    holiday.getHolidayId(), e);
        }
        return false;
    }

    // ================= DELETE =================

    @Override
    public boolean deleteHoliday(int holidayId) {

        String sql = "DELETE FROM holidays WHERE holiday_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, holidayId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while deleting holiday id={}",
                    holidayId, e);
        }
        return false;
    }

    // ================= VALIDATION =================

    @Override
    public boolean existsByDate(java.util.Date holidayDate) {

        String sql = "SELECT 1 FROM holidays WHERE holiday_date = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(holidayDate.getTime()));
            return ps.executeQuery().next();

        } catch (Exception e) {
            logger.error("Error while checking holiday date={}",
                    holidayDate, e);
        }
        return false;
    }

    @Override
    public boolean existsByName(String holidayName) {

        String sql = """
                SELECT 1
                FROM holidays
                WHERE LOWER(holiday_name) = LOWER(?)
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, holidayName);
            return ps.executeQuery().next();

        } catch (Exception e) {
            logger.error("Error while checking holiday name={}",
                    holidayName, e);
        }
        return false;
    }
}