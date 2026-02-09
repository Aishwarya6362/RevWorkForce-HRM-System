package com.revworkforce.dao;

import com.revworkforce.enums.GoalPriority;
import com.revworkforce.enums.GoalStatus;
import com.revworkforce.model.Goal;
import com.revworkforce.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * GoalDAOImpl handles all database operations
 * related to employee and team goals.
 */
public class GoalDAOImpl implements GoalDAO {

    private static final Logger logger =
            LogManager.getLogger(GoalDAOImpl.class);

    // ================= CREATE =================

    @Override
    public boolean addGoal(Goal goal) {

        String sql = """
            INSERT INTO goals
            VALUES (goal_seq.NEXTVAL, ?, ?, ?, ?, ?, 'NOT_STARTED')
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, goal.getEmpId());
            ps.setString(2, goal.getGoalDescription());
            ps.setDate(3, goal.getDeadline());
            ps.setString(4, goal.getPriority().name());
            ps.setString(5, goal.getSuccessMetric());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while adding goal for empId={}",
                    goal.getEmpId(), e);
        }
        return false;
    }

    // ================= READ (EMPLOYEE) =================

    @Override
    public List<Goal> getMyGoals(int empId) {

        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE emp_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Goal g = new Goal();
                g.setGoalId(rs.getInt("goal_id"));
                g.setGoalDescription(rs.getString("goal_description"));
                g.setDeadline(rs.getDate("deadline"));

                // ✅ FIXED ENUM HANDLING
                g.setPriority(
                        GoalPriority.valueOf(
                                rs.getString("priority")
                                        .trim()
                                        .toUpperCase()
                        )
                );

                g.setProgressStatus(
                        mapStatus(rs.getString("progress_status"))
                );

                goals.add(g);
            }

        } catch (Exception e) {
            logger.error("Error while fetching goals for empId={}", empId, e);
        }
        return goals;
    }

    // ================= READ (MANAGER) =================

    @Override
    public List<Goal> getTeamGoals(int managerId) {

        List<Goal> goals = new ArrayList<>();

        String sql = """
            SELECT g.*
            FROM goals g
            JOIN employee e ON g.emp_id = e.emp_id
            WHERE e.manager_id = ?
        """;

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Goal g = new Goal();
                g.setGoalId(rs.getInt("goal_id"));
                g.setGoalDescription(rs.getString("goal_description"));
                g.setProgressStatus(
                        mapStatus(rs.getString("progress_status"))
                );
                goals.add(g);
            }

        } catch (Exception e) {
            logger.error("Error while fetching team goals managerId={}",
                    managerId, e);
        }
        return goals;
    }

    // ================= READ (ADMIN) =================
    @Override
    public List<Goal> getAllGoals() {

        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Goal g = new Goal();
                g.setGoalId(rs.getInt("goal_id"));
                g.setEmpId(rs.getInt("emp_id"));
                g.setGoalDescription(rs.getString("goal_description"));
                g.setProgressStatus(
                        mapStatus(rs.getString("progress_status"))
                );
                goals.add(g);
            }

        } catch (Exception e) {
            // optional: comment this if you want ZERO logs
            logger.error("Error while fetching all goals", e);
        }
        return goals;
    }
    // ================= UPDATE =================

    @Override
    public boolean updateGoalStatus(int goalId, String status) {

        String sql = "UPDATE goals SET progress_status = ? WHERE goal_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);   // already ENUM.name()
            ps.setInt(2, goalId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error while updating goal status goalId={}", goalId, e);
        }
        return false;
    }
    private GoalStatus mapStatus(String dbStatus) {

        if (dbStatus == null) {
            return GoalStatus.NOT_STARTED;
        }

        dbStatus = dbStatus.trim().toUpperCase();

        // legacy / bad data handling
        switch (dbStatus) {
            case "END":
            case "ENDED":
            case "DONE":
                return GoalStatus.COMPLETED;

            case "IN PROGRESS":
                return GoalStatus.IN_PROGRESS;

            case "NOT STARTED":
                return GoalStatus.NOT_STARTED;

            default:
                try {
                    return GoalStatus.valueOf(dbStatus.replace(" ", "_"));
                } catch (Exception e) {
                    // 🚫 DO NOT log, DO NOT throw
                    return GoalStatus.NOT_STARTED;
                }
        }
    }
}