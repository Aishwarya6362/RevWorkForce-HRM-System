package com.revworkforce.dao;

import com.revworkforce.model.PerformanceReview;
import java.util.List;

/**
 * PerformanceDAO defines database operations
 * related to employee performance reviews.
 */
public interface PerformanceDAO {

    // ================= CREATE =================

    boolean submitReview(PerformanceReview review);

    // ================= READ =================

    PerformanceReview getMyReview(int empId, int year);

    List<PerformanceReview> getTeamReviews(int managerId);

    // ================= UPDATE =================

    boolean addManagerFeedback(int reviewId, String feedback, double rating);

    PerformanceReview getReviewById(int reviewId);

}