package com.revworkforce.dao;

import com.revworkforce.model.PerformanceReview;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PerformanceDAOImpl handles database operations
 * for performance review management.
 */
public class PerformanceDAOImpl implements PerformanceDAO {

    private static final Logger logger =
            LogManager.getLogger(PerformanceDAOImpl.class);

    // ================= CREATE =================

    @Override
    public boolean submitReview(PerformanceReview review) {

        String sql = """
                INSERT INTO performance_review
                VALUES (perf_seq.NEXTVAL, ?, ?, ?, ?, ?, NULL, NULL, 'SUBMITTED')
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, review.getEmpId());
            ps.setInt(2, review.getReviewYear());
            ps.setString(3, review.getAchievements());
            ps.setString(4, review.getImprovements());
            ps.setDouble(5, review.getSelfRating());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error(
                    "Error while submitting performance review empId={} year={}",
                    review.getEmpId(), review.getReviewYear(), e
            );
        }
        return false;
    }

    // ================= READ (EMPLOYEE) =================

    @Override
    public PerformanceReview getMyReview(int empId, int year) {

        String sql = """
                SELECT *
                FROM performance_review
                WHERE emp_id = ? AND review_year = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PerformanceReview pr = new PerformanceReview();
                pr.setReviewId(rs.getInt("review_id"));
                pr.setAchievements(rs.getString("achievements"));
                pr.setImprovements(rs.getString("improvements"));
                pr.setSelfRating(rs.getDouble("self_rating"));
                pr.setManagerFeedback(rs.getString("manager_feedback"));
                pr.setManagerRating(rs.getDouble("manager_rating"));
                pr.setStatus(rs.getString("status"));
                return pr;
            }

        } catch (Exception e) {
            logger.error(
                    "Error while fetching performance review empId={} year={}",
                    empId, year, e
            );
        }
        return null;
    }

    // ================= READ (MANAGER) =================

    @Override
    public List<PerformanceReview> getTeamReviews(int managerId) {

        List<PerformanceReview> reviews = new ArrayList<>();

        String sql = """
                SELECT pr.review_id, e.name, pr.review_year, pr.status
                FROM performance_review pr
                JOIN employee e ON pr.emp_id = e.emp_id
                WHERE e.manager_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PerformanceReview pr = new PerformanceReview();
                pr.setReviewId(rs.getInt("review_id"));
                pr.setReviewYear(rs.getInt("review_year"));
                pr.setStatus(rs.getString("status"));
                reviews.add(pr);
            }

        } catch (Exception e) {
            logger.error(
                    "Error while fetching team performance reviews managerId={}",
                    managerId, e
            );
        }
        return reviews;
    }

    // ================= UPDATE =================

    @Override
    public boolean addManagerFeedback(int reviewId, String feedback, double rating) {

        String sql = """
                UPDATE performance_review
                SET manager_feedback = ?, manager_rating = ?, status = 'REVIEWED'
                WHERE review_id = ?
                """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, feedback);
            ps.setDouble(2, rating);
            ps.setInt(3, reviewId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error(
                    "Error while adding manager feedback reviewId={}",
                    reviewId, e
            );
        }
        return false;
    }
    @Override
    public PerformanceReview getReviewById(int reviewId) {

        String sql = """
        SELECT pr.review_id,
               pr.review_year,
               pr.achievements,
               pr.improvements,
               pr.self_rating,
               pr.status,
               e.name
        FROM performance_review pr
        JOIN employee e ON pr.emp_id = e.emp_id
        WHERE pr.review_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PerformanceReview pr = new PerformanceReview();
                pr.setReviewId(rs.getInt("review_id"));
                pr.setReviewYear(rs.getInt("review_year"));
                pr.setAchievements(rs.getString("achievements"));
                pr.setImprovements(rs.getString("improvements"));
                pr.setSelfRating(rs.getDouble("self_rating"));
                pr.setStatus(rs.getString("status"));
                pr.setEmployeeName(rs.getString("name"));
                return pr;
            }

        } catch (Exception e) {
            logger.error("Error fetching reviewId={}", reviewId, e);
        }
        return null;
    }

}