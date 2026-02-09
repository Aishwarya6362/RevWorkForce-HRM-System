package com.revworkforce.model;

import java.sql.Date;

/**
 * Holiday model represents company holidays.
 *
 * This class is used to hold holiday information
 * such as date, name, and optional description.
 *
 * It is shared across DAO, Service, and UI layers.
 */
public class Holiday {

    // ===== IDENTIFIER =====
    private int holidayId;

    // ===== HOLIDAY DETAILS =====
    private String holidayName;
    private Date holidayDate;
    private String description;   // Optional notes (festival / national holiday)

    // ===== GETTERS & SETTERS =====

    public int getHolidayId() {
        return holidayId;
    }
    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }
    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}