package com.revworkforce.dao;

import com.revworkforce.model.Holiday;

import java.util.Date;
import java.util.List;

/**
 * HolidayDAO defines database operations
 * related to company holidays.
 */
public interface HolidayDAO {

    // ================= READ =================

    List<Holiday> getAllHolidays();

    // ================= CREATE =================

    boolean addHoliday(Holiday holiday);

    // ================= UPDATE =================

    boolean updateHoliday(Holiday holiday);

    // ================= DELETE =================

    boolean deleteHoliday(int holidayId);

    // ================= VALIDATION =================

    boolean existsByDate(Date holidayDate);

    boolean existsByName(String holidayName);
}