package com.revworkforce.test;

import com.revworkforce.service.HolidayService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class HolidayServiceTest {

    private final HolidayService service = new HolidayService();

    @Test
    void testViewHolidays() {
        assertNotNull(service.viewHolidays());
    }
}