package com.revworkforce.test;

import com.revworkforce.service.GoalService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GoalServiceTest {

    private final GoalService service = new GoalService();

    @Test
    void testViewGoals() {
        assertNotNull(service.viewMyGoals(1001));
    }
}