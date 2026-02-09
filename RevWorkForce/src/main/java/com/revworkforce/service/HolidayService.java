package com.revworkforce.service;

import com.revworkforce.dao.HolidayDAO;
import com.revworkforce.dao.HolidayDAOImpl;
import com.revworkforce.model.Holiday;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * HolidayService handles business logic
 * related to company holidays.
 */
public class HolidayService {

    private final HolidayDAO dao = new HolidayDAOImpl();
    private static final Logger logger =
            LogManager.getLogger(HolidayService.class);

    public List<Holiday> viewHolidays() {
        return dao.getAllHolidays();
    }

    public boolean addHoliday(Holiday holiday) {

        if (dao.existsByName(holiday.getHolidayName())) {
            logger.warn("Duplicate holiday attempt name={}",
                    holiday.getHolidayName());
            return false;
        }

        // ✅ FIX: date validation added
        if (dao.existsByDate(holiday.getHolidayDate())) {
            logger.warn("Duplicate holiday date attempt date={}",
                    holiday.getHolidayDate());
            return false;
        }

        logger.info("Holiday added name={}", holiday.getHolidayName());
        return dao.addHoliday(holiday);
    }


    public boolean updateHoliday(Holiday holiday) {
        logger.info("Holiday updated id={}", holiday.getHolidayId());
        return dao.updateHoliday(holiday);
    }

    public boolean deleteHoliday(int holidayId) {
        logger.info("Holiday deleted id={}", holidayId);
        return dao.deleteHoliday(holidayId);
    }
}