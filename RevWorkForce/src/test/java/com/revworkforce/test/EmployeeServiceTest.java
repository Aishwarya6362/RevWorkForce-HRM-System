package com.revworkforce.test;

import com.revworkforce.model.EmployeeProfile;
import com.revworkforce.service.EmployeeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private final EmployeeService service = new EmployeeService();

    @Test
    void testViewProfile() {
        EmployeeProfile profile = service.viewProfile(1001);
        assertNotNull(profile);
        assertEquals(1001, profile.getEmpId());
    }

    @Test
    void testInvalidEmployeeProfile() {
        EmployeeProfile profile = service.viewProfile(9999);
        assertNull(profile);
    }
}