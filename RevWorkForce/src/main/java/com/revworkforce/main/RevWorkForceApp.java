

package com.revworkforce.main;

import com.revworkforce.model.*;
import com.revworkforce.service.*;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class RevWorkForceApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        AuthService authService = new AuthService();
        EmployeeService employeeService = new EmployeeService();
        LeaveService leaveService = new LeaveService();
        AdminService adminService = new AdminService();
        NotificationService notificationService = new NotificationService();
        HolidayService holidayService = new HolidayService();
        PerformanceService performanceService = new PerformanceService();
        GoalService goalService = new GoalService();
        AnnouncementService announcementService = new AnnouncementService();

        // ================= LOGIN LOOP =================
        while (true) {

            System.out.println("\n===== Welcome to RevWorkForce =====");
            System.out.println("1. Login");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 0) break;
            if (option != 1) continue;

            System.out.print("Employee ID: ");
            int empId = scanner.nextInt();
            scanner.nextLine();

            String password;
            if (console != null) {
                password = new String(console.readPassword("Password: "));
            } else {
                System.out.print("Password: ");
                password = scanner.nextLine();
            }

            if (!authService.login(empId, password)) {
                System.out.println(" Invalid credentials");
                continue;
            }

            EmployeeProfile emp = employeeService.viewProfile(empId);
            String role = emp.getRoleName();

            // ===== Notifications =====
            List<Notification> notes =
                    notificationService.getUnreadNotifications(empId);

            if (!notes.isEmpty()) {
                System.out.println("\n🔔 Notifications:");
                notes.forEach(n -> System.out.println("- " + n.getMessage()));
                notificationService.markAsRead(empId);
            }

            // ================= MENU LOOP =================
            while (true) {

                MenuPrinter.printMenu(role);

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    System.out.println("Logged out ");
                    break;
                }

                // EMPLOYEE
                if (choice >= 1 && choice <= 17) {
                    MenuHandler.handleEmployeeMenu(
                            choice, empId, scanner, console, emp,
                            employeeService, authService,
                            leaveService, holidayService,
                            performanceService, goalService,
                            announcementService
                    );
                }

                // MANAGER
                else if (choice >= 18 && choice <= 23) {
                    if (role.equalsIgnoreCase("MANAGER")
                            || role.equalsIgnoreCase("ADMIN")) {

                        MenuHandler.handleManagerMenu(
                                choice, empId, scanner,
                                leaveService, performanceService, goalService
                        );
                    } else {
                        System.out.println(" Manager access only");
                    }
                }

                // ADMIN
                else if (choice >= 24 && choice <= 34) {
                    if (role.equalsIgnoreCase("ADMIN")) {

                        MenuHandler.handleAdminMenu(
                                choice,empId, scanner,
                                adminService, holidayService,
                                goalService, announcementService
                        );
                    } else {
                        System.out.println(" Admin access only");
                    }
                }

                else {
                    System.out.println(" Invalid option");
                }
            }
        }
        System.out.println("Thank you for using RevWorkForce ❤️");
    }
}