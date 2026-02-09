package com.revworkforce.model;

/**
 * PerformanceReview model represents an employee's
 * annual performance evaluation.
 *
 * It is used by:
 * - Employee (self review submission & viewing)
 * - Manager (reviewing team performance & feedback)
 */
public class PerformanceReview {

    // ===== IDENTIFIERS =====
    private int reviewId;
    private int empId;
    private int reviewYear;

    // ===== EMPLOYEE SELF REVIEW =====
    private String achievements;
    private String improvements;
    private double selfRating;

    // ===== MANAGER FEEDBACK =====
    private String managerFeedback;
    private double managerRating;

    // ===== REVIEW STATUS =====
    private String status;   // SUBMITTED / REVIEWED

    private String employeeName;

    // ===== GETTERS & SETTERS =====

    public int getReviewId() {
        return reviewId;
    }
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getReviewYear() {
        return reviewYear;
    }
    public void setReviewYear(int reviewYear) {
        this.reviewYear = reviewYear;
    }

    public String getAchievements() {
        return achievements;
    }
    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getImprovements() {
        return improvements;
    }
    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public double getSelfRating() {
        return selfRating;
    }
    public void setSelfRating(double selfRating) {
        this.selfRating = selfRating;
    }

    public String getManagerFeedback() {
        return managerFeedback;
    }
    public void setManagerFeedback(String managerFeedback) {
        this.managerFeedback = managerFeedback;
    }

    public double getManagerRating() {
        return managerRating;
    }
    public void setManagerRating(double managerRating) {
        this.managerRating = managerRating;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}