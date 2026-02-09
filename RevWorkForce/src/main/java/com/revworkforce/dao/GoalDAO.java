package com.revworkforce.dao;

import com.revworkforce.model.Goal;
import java.util.List;

/**
 * GoalDAO defines database operations related to
 * employee and team goals.
 *
 * It supports goal creation, progress updates,
 * and role-based goal viewing.
 */
public interface GoalDAO {

    // Adds a new goal for an employee
    boolean addGoal(Goal goal);

    // Returns goals created by a specific employee
    List<Goal> getMyGoals(int empId);

    // Updates the progress/status of a goal
    boolean updateGoalStatus(int goalId, String status);

    // Returns goals of employees reporting to a manager
    List<Goal> getTeamGoals(int managerId);

    // Returns all goals in the system (admin view)
    List<Goal> getAllGoals();
}