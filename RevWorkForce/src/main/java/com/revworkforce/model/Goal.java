package com.revworkforce.model;

import com.revworkforce.enums.GoalPriority;
import com.revworkforce.enums.GoalStatus;

import java.sql.Date;

public class Goal {

    private int goalId;
    private int empId;
    private String goalDescription;
    private Date deadline;
    private GoalPriority priority;
    private GoalStatus progressStatus;
    private String successMetric;

    // ===== getters & setters =====

    public int getGoalId() {
        return goalId;
    }
    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getGoalDescription() {
        return goalDescription;
    }
    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public Date getDeadline() {
        return deadline;
    }
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public GoalPriority getPriority() {
        return priority;
    }
    public void setPriority(GoalPriority priority) {
        this.priority = priority;
    }

    public GoalStatus getProgressStatus() {
        return progressStatus;
    }
    public void setProgressStatus(GoalStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getSuccessMetric() {
        return successMetric;
    }
    public void setSuccessMetric(String successMetric) {
        this.successMetric = successMetric;
    }
}