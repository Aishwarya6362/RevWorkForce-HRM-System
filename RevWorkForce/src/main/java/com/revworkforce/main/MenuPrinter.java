


package com.revworkforce.main;

public class MenuPrinter {

    public static void printMenu(String role) {

        System.out.println("\n===== EMPLOYEE MENU =====");

        // ===== PROFILE =====
        System.out.println("1. View My Profile");
        System.out.println("2. Edit My Profile");
        System.out.println("3. Change Password");
        System.out.println("4. View Manager Details");

        // ===== LEAVE =====
        System.out.println("5. View Leave Balance");
        System.out.println("6. Apply Leave");
        System.out.println("7. View My Leave Requests");
        System.out.println("8. Cancel Pending Leave");
        System.out.println("9. View Company Holidays");

        // ===== PERFORMANCE =====
        System.out.println("10. Submit Performance Review");
        System.out.println("11. View My Performance Review");

        // ===== GOALS =====
        System.out.println("12. Add Goal");
        System.out.println("13. View My Goals");
        System.out.println("14. Update Goal Progress");

        // ===== INFO =====
        System.out.println("15. View Upcoming Birthdays");
        System.out.println("16. View Work Anniversaries");
        System.out.println("17. View Announcements");

        // ===== MANAGER =====
        if ("MANAGER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
            System.out.println("\n===== MANAGER MENU =====");
            System.out.println("18. View Team Leave Requests");
            System.out.println("19. Approve / Reject Leave");
            System.out.println("20. View Team Performance Reviews");
            System.out.println("21. Add Manager Feedback");
            System.out.println("22. View Team Goals");
            System.out.println("23. Update Team Goal Status");
        }

        // ===== ADMIN =====
        if ("ADMIN".equalsIgnoreCase(role)) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("24. Add Employee");
            System.out.println("25. Update Employee");
            System.out.println("26. Activate / Deactivate Employee");
            System.out.println("27. View All Employees");
            System.out.println("28. Search Employee");
            System.out.println("29. Change Employee Manager");
            System.out.println("30. Add Holiday");
            System.out.println("31. Update Holiday");
            System.out.println("32. Delete Holiday");
            System.out.println("33. View All Goals");
            System.out.println("34. Add Announcement");
        }

        System.out.println("\n0. Logout");
        System.out.print("Choose option: ");
    }
}