package com.revworkforce.main;

import com.revworkforce.enums.*;
import com.revworkforce.model.*;
import com.revworkforce.service.*;

import java.io.Console;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {

    /* ========================== EMPLOYEE ========================== */
    public static void handleEmployeeMenu(
            int choice,
            int empId,
            Scanner sc,
            Console console,
            EmployeeProfile emp,
            EmployeeService empService,
            AuthService authService,
            LeaveService leaveService,
            HolidayService holidayService,
            PerformanceService performanceService,
            GoalService goalService,
            AnnouncementService announcementService) {

        switch (choice) {

            case 1 -> {
                EmployeeProfile p = empService.viewProfile(empId);

                System.out.println("\n===== MY PROFILE =====");
                System.out.println("Employee ID        : " + p.getEmpId());
                System.out.println("Name               : " + p.getName());
                System.out.println("Email              : " + p.getEmail());
                System.out.println("Phone              : " + p.getPhone());
                System.out.println("Address            : " + p.getAddress());
                System.out.println("Emergency Contact  : " + p.getEmergencyContact());
                System.out.println("Department         : " + p.getDepartmentName());
                System.out.println("Role               : " + p.getRoleName());
                System.out.println("Manager            : " +
                        (p.getManagerName() != null ? p.getManagerName() : "N/A"));
            }

            case 2 -> {

                // get current values
                String phone = emp.getPhone();
                String address = emp.getAddress();
                String emergency = emp.getEmergencyContact();

                System.out.print("Update phone? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("Enter new phone: ");
                    phone = sc.nextLine();
                }

                System.out.print("Update address? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("Enter new address: ");
                    address = sc.nextLine();
                }

                System.out.print("Update emergency contact? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("Enter new emergency contact: ");
                    emergency = sc.nextLine();
                }

                boolean updated =
                        empService.updateMyProfile(empId, phone, address, emergency);

                System.out.println(
                        updated
                                ? "Profile updated successfully"
                                : "Profile update failed"
                );
            }


            case 3 -> {
                String cur, n, cn;
                if (console != null) {
                    cur = new String(console.readPassword("Current Password: "));
                    n = new String(console.readPassword("New Password: "));
                    cn = new String(console.readPassword("Confirm Password: "));
                } else {
                    System.out.print("Current Password: ");
                    cur = sc.nextLine();
                    System.out.print("New Password: ");
                    n = sc.nextLine();
                    System.out.print("Confirm Password: ");
                    cn = sc.nextLine();
                }
                if (!n.equals(cn)) {
                    System.out.println("Password mismatch");
                    break;
                }
                System.out.println(
                        authService.changePassword(empId, cur, n)
                                ? "Password changed successfully"
                                : "Invalid current password"
                );
            }

            case 4 -> {
                if (emp.getManagerName() == null) {
                    System.out.println("Manager: Not assigned");
                } else {
                    System.out.println("Manager: " + emp.getManagerName());
                }
            }


            case 5 -> leaveService.viewBalance(empId)
                    .forEach(l ->
                            System.out.println(l.getLeaveName() + " : " + l.getBalance()));

            case 6 -> {
                LeaveRequest lr = new LeaveRequest();
                lr.setEmpId(empId);

                System.out.print("Leave Type ID: ");
                lr.setLeaveTypeId(sc.nextInt());
                sc.nextLine();

                System.out.print("Start Date (yyyy-mm-dd): ");
                lr.setStartDate(Date.valueOf(sc.nextLine()));

                System.out.print("End Date (yyyy-mm-dd): ");
                lr.setEndDate(Date.valueOf(sc.nextLine()));

                System.out.print("Reason: ");
                lr.setReason(sc.nextLine());

                System.out.println(
                        leaveService.applyLeave(lr)
                                ? "Leave applied successfully"
                                : "Leave application failed"
                );
            }

            case 7 -> {
                List<LeaveRequest> leaves =
                        leaveService.viewMyLeaves(empId);

                if (leaves.isEmpty()) {
                    System.out.println("You have not applied for any leave yet.");
                } else {
                    System.out.println("\n===== MY LEAVE REQUESTS =====");
                    leaves.forEach(l ->
                            System.out.println(
                                    "Leave ID : " + l.getLeaveRequestId() +
                                            " | Type : " + l.getLeaveName() +
                                            " | Status : " + l.getStatus()
                            )
                    );
                }
            }


            case 8 -> {
                System.out.print("Enter Leave Request ID to cancel: ");
                int id = sc.nextInt();
                sc.nextLine();

                boolean cancelled =
                        leaveService.cancelLeave(id, empId);

                System.out.println(
                        cancelled
                                ? "Leave cancelled successfully"
                                : "No pending leave found with this ID"
                );
            }


            case 9 -> holidayService.viewHolidays()
                    .forEach(h ->
                            System.out.println(
                                    h.getHolidayDate() + " | " +
                                            h.getHolidayName() +
                                            (h.getDescription() != null ? " - " + h.getDescription() : "")
                            ));

            case 10 -> {
                PerformanceReview r = new PerformanceReview();
                r.setEmpId(empId);

                System.out.print("Review Year: ");
                r.setReviewYear(sc.nextInt());
                sc.nextLine();

                System.out.print("Achievements: ");
                r.setAchievements(sc.nextLine());

                System.out.print("Improvements: ");
                r.setImprovements(sc.nextLine());

                System.out.print("Self Rating (1–5): ");
                r.setSelfRating(sc.nextDouble());

                System.out.println(
                        performanceService.submitReview(r)
                                ? "Review submitted"
                                : "Submission failed"
                );
            }

            case 11 -> {
                System.out.print("Review Year: ");
                int year = sc.nextInt();

                PerformanceReview r =
                        performanceService.viewMyReview(empId, year);

                if (r == null) {
                    System.out.println("No performance review found for this year");
                } else {
                    System.out.println("Status: " + r.getStatus());
                    System.out.println("Achievements: " + r.getAchievements());
                    System.out.println("Improvements: " + r.getImprovements());
                    System.out.println("Self Rating: " + r.getSelfRating());

                    if (emp.getManagerId() == null) {
                        System.out.println("Manager Feedback: Not applicable");
                    } else {
                        System.out.println("Manager Feedback: " +
                                (r.getManagerFeedback() != null
                                        ? r.getManagerFeedback()
                                        : "Pending"));
                    }
                }
            }


            case 12 -> {
                Goal g = new Goal();
                g.setEmpId(empId);

                System.out.print("Goal Description: ");
                g.setGoalDescription(sc.nextLine());

                System.out.print("Deadline (yyyy-mm-dd): ");
                g.setDeadline(Date.valueOf(sc.nextLine()));

                System.out.print("Priority (HIGH / MEDIUM / LOW): ");
                g.setPriority(GoalPriority.valueOf(sc.nextLine().toUpperCase()));

                System.out.print("Success Metric: ");
                g.setSuccessMetric(sc.nextLine());

                System.out.println(
                        goalService.addGoal(g)
                                ? "Goal added successfully"
                                : "Failed to add goal"
                );
            }

            case 13 -> {
                System.out.println("\n===== MY GOALS =====");
                List<Goal> goals = goalService.viewMyGoals(empId);

                if (goals.isEmpty()) {
                    System.out.println("No goals found");
                } else {
                    goals.forEach(g ->
                            System.out.println(
                                    "Goal ID  : " + g.getGoalId() + "\n" +
                                            "Goal     : " + g.getGoalDescription() + "\n" +
                                            "Priority : " + g.getPriority() + "\n" +
                                            "Status   : " + g.getProgressStatus() + "\n" +
                                            "-----------------------------"
                            ));
                }
            }

            case 14 -> {
                System.out.print("Goal ID: ");
                int gid = sc.nextInt();
                sc.nextLine();

                System.out.print("New Status (NOT_STARTED / IN_PROGRESS / COMPLETED): ");
                GoalStatus st = GoalStatus.valueOf(sc.nextLine().toUpperCase());

                System.out.println(
                        goalService.updateGoalStatus(gid, st)
                                ? "Goal updated"
                                : "Update failed"
                );
            }

            case 15 -> {
                List<EmployeeProfile> list =
                        empService.getUpcomingBirthdays();

                if (list.isEmpty()) {
                    System.out.println("No upcoming birthdays in the next 7 days.");
                } else {
                    System.out.println("\n===== UPCOMING BIRTHDAYS =====");
                    list.forEach(e ->
                            System.out.println(
                                    "Emp ID : " + e.getEmpId() +
                                            " | Name : " + e.getName() +
                                            " | DOB : " + e.getDob()
                            )
                    );
                }
            }

            case 16 -> {
                List<EmployeeProfile> list =
                        empService.getWorkAnniversaries();

                if (list.isEmpty()) {
                    System.out.println("No work anniversaries this month.");
                } else {
                    System.out.println("\n===== WORK ANNIVERSARIES =====");
                    list.forEach(e ->
                            System.out.println(
                                    "Name : " + e.getName() +
                                            " | Years Completed : " + e.getYearsCompleted()
                            )
                    );
                }
            }
            case 17 -> {
                List<Announcement> announcements =
                        announcementService.viewAnnouncements();

                if (announcements.isEmpty()) {
                    System.out.println("\n📢 No announcements available at the moment.\n");
                } else {
                    System.out.println("\n===== ANNOUNCEMENTS =====");
                    announcements.forEach(a -> {
                        System.out.println("Title   : " + a.getTitle());
                        System.out.println("Message : " + a.getMessage());
                        System.out.println("Posted By Employee ID : " + a.getCreatedBy());
                        System.out.println("Date    : " + a.getCreatedDate());
                        System.out.println("-----------------------------------");
                    });
                }
            }

        }
    }

    /* ========================== MANAGER ========================== */
    public static void handleManagerMenu(
            int choice,
            int managerId,
            Scanner sc,
            LeaveService leaveService,
            PerformanceService performanceService,
            GoalService goalService) {

        switch (choice) {

            case 18 -> leaveService.viewTeamLeaves(managerId)
                    .forEach(l ->
                            System.out.println(
                                    "ReqID: " + l.getLeaveRequestId() +
                                            " | Emp: " + l.getEmployeeName() +
                                            " | Leave: " + l.getLeaveName() +
                                            " | Status: " + l.getStatus()
                            ));

            case 19 -> {
                System.out.print("Leave Request ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Approve / Reject (A/R): ");
                String d = sc.nextLine();

                System.out.println(
                        leaveService.updateLeaveStatus(
                                id,
                                d.equalsIgnoreCase("A") ? "APPROVED" : "REJECTED",
                                "Manager decision")
                                ? "Leave updated"
                                : "Operation failed"
                );
            }

            case 20 -> performanceService.viewTeamReviews(managerId)
                    .forEach(r ->
                            System.out.println(
                                    "ReviewID: " + r.getReviewId() +
                                            " | Year: " + r.getReviewYear() +
                                            " | Status: " + r.getStatus()
                            ));

            case 21 -> {
                System.out.print("Review ID: ");
                int reviewId = sc.nextInt();
                sc.nextLine();

                PerformanceReview pr = performanceService.viewReviewById(reviewId);

                if (pr == null) {
                    System.out.println(" Review not found");
                    return;
                }

                System.out.println("\n===== PERFORMANCE REVIEW =====");
                System.out.println("Employee     : " + pr.getEmployeeName());
                System.out.println("Year         : " + pr.getReviewYear());
                System.out.println("Achievements : " + pr.getAchievements());
                System.out.println("Improvements : " + pr.getImprovements());
                System.out.println("Self Rating  : " + pr.getSelfRating());
                System.out.println("Status       : " + pr.getStatus());
                System.out.println("--------------------------------");

                System.out.print("Feedback: ");
                String feedback = sc.nextLine();

                System.out.print("Rating (1–5): ");
                double rating = sc.nextDouble();

                boolean updated =
                        performanceService.addManagerFeedback(reviewId, feedback, rating);

                System.out.println(updated ? "Feedback added" : "Failed to add feedback");
            }

                case 22 -> goalService.viewTeamGoals(managerId)
                    .forEach(g ->
                            System.out.println(
                                    g.getGoalId() + " | " +
                                            g.getGoalDescription() + " | " +
                                            g.getProgressStatus()
                            ));

            case 23 -> {
                System.out.print("Goal ID: ");
                int gid = sc.nextInt();
                sc.nextLine();

                System.out.print(
                        "New Status (NOT_STARTED / IN_PROGRESS / COMPLETED): "
                );
                String input = sc.nextLine().toUpperCase();

                // ✅ Validation to prevent crash
                if (!input.equals("NOT_STARTED")
                        && !input.equals("IN_PROGRESS")
                        && !input.equals("COMPLETED")) {

                    System.out.println(
                            " Invalid status. Please choose a valid option."
                    );
                    return;
                }

                GoalStatus st = GoalStatus.valueOf(input);

                boolean updated = goalService.updateGoalStatus(gid, st);

                if (updated) {
                    System.out.println(" Goal updated successfully");
                } else {
                    System.out.println(" Goal update failed");
                }
            }

        }
    }

    /* ========================== ADMIN ========================== */
    public static void handleAdminMenu(
            int choice,
            int empId,
            Scanner sc,
            AdminService adminService,
            HolidayService holidayService,
            GoalService goalService,
            AnnouncementService announcementService) {

        switch (choice) {

            case 24 -> {
                Employee e = new Employee();

                System.out.print("Employee ID: ");
                e.setEmpId(sc.nextInt());
                sc.nextLine();

                System.out.print("Name: ");
                e.setName(sc.nextLine());

                System.out.print("Email: ");
                e.setEmail(sc.nextLine());

                System.out.print("Phone: ");
                e.setPhone(sc.nextLine());

                System.out.print("Address: ");
                e.setAddress(sc.nextLine());

                System.out.print("DOB (yyyy-mm-dd): ");
                e.setDob(Date.valueOf(sc.nextLine()));

                System.out.print("Designation: ");
                e.setDesignation(sc.nextLine());

                System.out.print("Department ID: ");
                e.setDepartmentId(sc.nextInt());

                System.out.print("Manager ID (0 if none): ");
                int mid = sc.nextInt();
                e.setManagerId(mid == 0 ? null : mid);

                System.out.print("Role ID: ");
                e.setRoleId(sc.nextInt());
                sc.nextLine();

                System.out.print("Password: ");
                e.setPassword(sc.nextLine());

                adminService.addEmployee(e);
                System.out.println("Employee added");
            }

            case 25 -> {
                Employee e = new Employee();

                System.out.print("Employee ID: ");
                int id = sc.nextInt();
                sc.nextLine();
                e.setEmpId(id);

                boolean updateSelected = false; // ✅ KEY FIX

                // PHONE
                System.out.print("Update Phone? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Phone: ");
                    e.setPhone(sc.nextLine());
                    updateSelected = true;
                }

                // ADDRESS
                System.out.print("Update Address? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Address: ");
                    e.setAddress(sc.nextLine());
                    updateSelected = true;
                }

                // DESIGNATION
                System.out.print("Update Designation? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Designation: ");
                    e.setDesignation(sc.nextLine());
                    updateSelected = true;
                }

                // DEPARTMENT
                System.out.print("Update Department? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Department ID: ");
                    e.setDepartmentId(sc.nextInt());
                    sc.nextLine();
                    updateSelected = true;
                }

                // ✅ IMPORTANT GUARD
                if (!updateSelected) {
                    System.out.println(" No fields selected for update");
                    break;
                }

                boolean updated = adminService.updateEmployee(e);
                System.out.println(updated
                        ? " Employee updated successfully"
                        : " Employee update failed");
            }



            case 26 -> {
                System.out.print("Employee ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Status (ACTIVE/INACTIVE): ");
                String status = sc.nextLine().toUpperCase();

                adminService.updateEmployeeStatus(id, status);
                System.out.println("Status updated");
            }

            case 27 -> adminService.getAllEmployees()
                    .forEach(e ->
                            System.out.println(
                                    e.getEmpId() + " | " +
                                            e.getName() + " | " +
                                            e.getStatus()
                            ));

            case 28 -> {
                System.out.print("Search keyword: ");
                String key = sc.nextLine();

                adminService.searchEmployees(key)
                        .forEach(e ->
                                System.out.println(
                                        e.getEmpId() + " | " + e.getName()
                                ));
            }

            case 29 -> {
                System.out.print("Employee ID: ");
                int eid = sc.nextInt();

                System.out.print("New Manager ID: ");
                int mid = sc.nextInt();

                adminService.changeManager(eid, mid == 0 ? null : mid);
                System.out.println("Manager changed");
            }

            case 30 -> {
                Holiday h = new Holiday();

                System.out.print("Holiday Name: ");
                h.setHolidayName(sc.nextLine());

                System.out.print("Holiday Date: ");
                h.setHolidayDate(Date.valueOf(sc.nextLine()));

                System.out.print("Description: ");
                h.setDescription(sc.nextLine());

                holidayService.addHoliday(h);
                System.out.println("Holiday added");
            }

            case 31 -> {
                Holiday h = new Holiday();

                System.out.print("Enter Holiday ID to update: ");
                h.setHolidayId(sc.nextInt());
                sc.nextLine();

                System.out.print("Update holiday name? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Holiday Name: ");
                    h.setHolidayName(sc.nextLine());
                }

                System.out.print("Update holiday date? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Holiday Date (yyyy-mm-dd): ");
                    h.setHolidayDate(Date.valueOf(sc.nextLine()));
                }

                System.out.print("Update description? (Y/N): ");
                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    System.out.print("New Description: ");
                    h.setDescription(sc.nextLine());
                }

                boolean updated =
                        holidayService.updateHoliday(h);

                System.out.println(
                        updated
                                ? "Holiday updated successfully"
                                : "Holiday update failed (ID may not exist)"
                );
            }


            case 32 -> {
                System.out.print("Enter Holiday ID to delete: ");
                int hid = sc.nextInt();
                sc.nextLine();

                boolean deleted = holidayService.deleteHoliday(hid);

                if (deleted) {
                    System.out.println("Holiday deleted successfully");
                } else {
                    System.out.println("Holiday not found. Deletion failed.");
                }
            }


            case 33 -> goalService.viewAllGoals()
                    .forEach(g ->
                            System.out.println(
                                    "EmpID: " + g.getEmpId() +
                                            " | Goal: " + g.getGoalDescription() +
                                            " | Status: " + g.getProgressStatus()
                            ));

            case 34 -> {
                Announcement a = new Announcement();

                System.out.print("Title: ");
                a.setTitle(sc.nextLine());

                System.out.print("Message: ");
                a.setMessage(sc.nextLine());

                // ✅ CRITICAL FIX: set logged-in admin as creator
                a.setCreatedBy(empId);

                boolean added = announcementService.addAnnouncement(a);

                if (added) {
                    System.out.println("Announcement posted successfully");
                } else {
                    System.out.println("Failed to post announcement");
                }
            }

        }
    }
}