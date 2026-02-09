package com.revworkforce.service;

import com.revworkforce.dao.GoalDAO;
import com.revworkforce.dao.GoalDAOImpl;
import com.revworkforce.enums.GoalStatus;
import com.revworkforce.model.Goal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * GoalService handles employee goal management logic.
 */
public class GoalService {

    private final GoalDAO dao = new GoalDAOImpl();
    private static final Logger logger =
            LogManager.getLogger(GoalService.class);

    // ===== EMPLOYEE OPERATIONS =====

    public boolean addGoal(Goal goal) {
        logger.info("Adding goal for empId={}", goal.getEmpId());
        return dao.addGoal(goal);
    }

    public List<Goal> viewMyGoals(int empId) {
        return dao.getMyGoals(empId);
    }

    public boolean updateGoalStatus(int goalId, GoalStatus status) {
        logger.info("Updating goalId={} status={}", goalId, status);
        return dao.updateGoalStatus(goalId, status.name());
    }

    // ===== MANAGER / ADMIN OPERATIONS =====

    public List<Goal> viewTeamGoals(int managerId) {
        return dao.getTeamGoals(managerId);
    }

    public List<Goal> viewAllGoals() {
        return dao.getAllGoals();
    }
}