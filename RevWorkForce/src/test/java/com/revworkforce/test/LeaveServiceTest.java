package com.revworkforce.test;

import com.revworkforce.model.LeaveRequest;
import com.revworkforce.service.LeaveService;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class LeaveServiceTest {

    private final LeaveService service = new LeaveService();

    @Test
    void testViewLeaveBalance() {
        assertNotNull(service.viewBalance(1001));
    }

//    @Test
//    void testApplyLeave() {
//        LeaveRequest lr = new LeaveRequest();
//        lr.setEmpId(3004);
//        lr.setLeaveTypeId(1);
//        lr.setStartDate(Date.valueOf("2026-02-11"));
//        lr.setEndDate(Date.valueOf("2026-02-12"));
//
//        assertTrue(service.applyLeave(lr));
//    }

    @Test
    void testCancelLeaveInvalid() {
        assertFalse(service.cancelLeave(9999, 1001));
    }
}