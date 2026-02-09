package com.revworkforce.test;

import com.revworkforce.model.PerformanceReview;
import com.revworkforce.service.PerformanceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PerformanceServiceTest {

    private final PerformanceService service = new PerformanceService();


//    @Test
//    void testSubmitReview() {
//        PerformanceReview pr = new PerformanceReview();
//        pr.setEmpId(1001);
//        pr.setReviewYear(2030);   // ✅ year that does NOT exist in DB
//        pr.setAchievements("Good work");
//        pr.setImprovements("Time management");
//        pr.setSelfRating(4.2);
//
//        assertTrue(service.submitReview(pr));
//    }
//
//    @Test
//    void testViewMyReview() {
//        assertNotNull(service.viewMyReview(1001, 2025));
//    }
}