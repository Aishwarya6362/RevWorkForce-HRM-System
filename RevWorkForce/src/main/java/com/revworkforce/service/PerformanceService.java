package com.revworkforce.service;

import com.revworkforce.dao.PerformanceDAO;
import com.revworkforce.dao.PerformanceDAOImpl;
import com.revworkforce.model.PerformanceReview;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * PerformanceService handles performance review logic.
 */
public class PerformanceService {

    private final PerformanceDAO dao = new PerformanceDAOImpl();
    private static final Logger logger =
            LogManager.getLogger(PerformanceService.class);

    public boolean submitReview(PerformanceReview pr) {

        // 🔒 Prevent duplicate review for same year
        PerformanceReview existing =
                dao.getMyReview(pr.getEmpId(), pr.getReviewYear());

        if (existing != null) {
            System.out.println(
                    "Performance review already submitted for this year."
            );
            return false;
        }

        logger.info("Submitting performance review empId={} year={}",
                pr.getEmpId(), pr.getReviewYear());

        return dao.submitReview(pr);
    }



    public PerformanceReview viewMyReview(int empId, int year) {
        return dao.getMyReview(empId, year);
    }

    public List<PerformanceReview> viewTeamReviews(int managerId) {
        return dao.getTeamReviews(managerId);
    }

    public boolean addManagerFeedback(
            int reviewId, String feedback, double rating) {

        logger.info("Manager feedback added reviewId={}", reviewId);
        return dao.addManagerFeedback(reviewId, feedback, rating);
    }
    public PerformanceReview viewReviewById(int reviewId) {
        return dao.getReviewById(reviewId);
    }

}