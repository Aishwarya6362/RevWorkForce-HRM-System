# RevWorkForce-HRM-System
A console-based role-driven Human Resource Management System built using Java, JDBC, Oracle SQL, Maven, Log4j, and JUnit.

RevWorkForce – Role-Based Human Resource Management System

Project Overview:
RevWorkForce is a console-based, role-driven Human Resource Management System developed using Java and Oracle SQL.
The application manages core HR operations such as employee management, leave processing, performance reviews, and notifications using secure role-based access control.
This project demonstrates real-world backend development concepts including authentication, authorization, database design, and clean layered architecture.

Key Features:
Role-based authentication and authorization (Employee, Manager, Admin)
Secure login with password hashing (SHA-256)
Employee profile management
Leave application and approval workflow
Leave balance tracking and validation
Performance review management
Goal tracking
Holiday management
Notification system
Audit logging for critical actions

Project Architecture:
The project follows a layered architecture to ensure separation of concerns:
<br>
Presentation Layer (Console Menus)<br>
        ↓<br>
Service Layer (Business Logic & Validation)<br>
        ↓<br>
DAO Layer (JDBC Database Operations)<br>
        ↓<br>
Database Layer (Oracle SQL)

Benefits:
Clean separation of responsibilities
Easy maintenance and scalability
Secure and controlled data access

Database Design:
The database is designed using normalized relational tables.
Design Principles Applied
Primary and foreign keys for referential integrity
Oracle sequences for unique identifier generation
Normalized schema to reduce redundancy
Separation of master and transactional data
Role-based relational mapping for hierarchy
Status-based access control (ACTIVE / INACTIVE users)
Audit-first design for traceability
Time-based data tracking for system events

Authentication & Authorization:
User credentials are validated using hashed passwords
Only ACTIVE employees are allowed to log in

Role-based menu access is enforced as follows:<br>

EMPLOYEE → Employee Menu
MANAGER  → Employee + Manager Menu
ADMIN    → Employee + Manager + Admin Menu

This approach prevents unauthorized access and ensures hierarchy-based operations.

Application Workflows--
Login & Authentication Workflow:<br>
User enters Employee ID and Password<br>
        ↓<br>
Password hashed using SHA-256<br>
        ↓<br>
Credentials validated from EMPLOYEE table<br>
        ↓<br>
Employee status check (ACTIVE / INACTIVE)<br>
        ↓<br>
Login Successful / Login Failed

Role-Based Access Control Workflow:
Login Success<br>
        ↓<br>
Fetch user role<br>
        ↓<br>
Display menus based on role

Employee Profile Management Workflow:<br>
View Profile<br>
        ↓<br>
Service validation<br>
        ↓<br>
DAO fetches profile using joins<br>
        ↓<br>
Display non-sensitive employee details

Update Profile<br>
        ↓<br>
Validate ACTIVE status<br>
        ↓<br>
Update phone, address, emergency contact

Leave Management Workflow:<br>
Employee applies for leave<br>
        ↓<br>
Initialize leave balance (if missing)<br>
        ↓<br>
Check overlapping leave dates<br>
        ↓<br>
Validate leave balance<br>
        ↓<br>
Manager exists?<br>
        ↓<br>
PENDING / Auto-APPROVED<br>
        ↓<br>
Update leave balance<br>
        ↓<br>
Create notification<br>
        ↓<br>
Create audit log

Notification Workflow:<br>
System action occurs<br>
        ↓<br>
Notification created (UNREAD)<br>
        ↓<br>
User views notifications<br>
        ↓<br>
Notifications marked as READ

Audit Logging Workflow:<br>
Critical action performed<br>
        ↓<br>
Audit entry created with timestamp

Goal Management Workflow:<br>
Employee creates goal<br>
        ↓<br>
Track progress<br>
        ↓<br>
Manager views team goals<br>
        ↓<br>
Admin views all goals

Performance Review Workflow:<br>
Employee submits self-review<br>
        ↓<br>
Status = SUBMITTED<br>
        ↓<br>
Manager adds feedback and rating<br>
        ↓<br>
Status = REVIEWED

Technology Stack:
Language: Java
Database: Oracle SQL
Connectivity: JDBC
Build Tool: Maven
Logging: Log4j
Testing: JUnit

Testing:
Unit tests implemented using JUnit
Service-layer testing for authentication, leave, notifications, and performance modules

Project Structure:
RevWorkForce<br>
│── src/main/java<br>
│   ├── model<br>
│   ├── dao<br>
│   ├── service<br>
│   ├── util<br>
│   └── main<br>
│<br>
│── src/test/java<br>
│   └── service tests<br>
│<br>
│── resources<br>
│   └── log4j2.xml<br>
│<br>
│── pom.xml<br>

How to Run the Project:
Clone the repository
Configure Oracle DB connection in DBUtil
Execute SQL scripts for tables and sequences
Run the Main class
Log in using valid employee credentials

Perfect 👍 I’ll update it clean and professional.

🔐 Default User Login Credentials

The system supports three predefined roles: Admin, Manager, and Employee.

🟦 Admin Login (Static – Predefined)
Role	Employee ID	Access Level
ADMIN	1001	Full System Access

This Admin account is static and used for system configuration and employee management.

🟦 Manager Logins (Predefined)
Role	Employee ID
MANAGER	2001
MANAGER	2002
MANAGER	2003
MANAGER	2004
MANAGER	2005

Managers have access to:

Employee features
Team leave approvals
Performance review feedback
Goal tracking for team members

🟦 Employee Logins (Sample Users)
Role	Employee ID
EMPLOYEE	3001
EMPLOYEE	3002

Employees can:

Apply for leave
View leave balance
Submit performance reviews
Set and track goals
View notifications and announcements

Project Summary:
RevWorkForce is a secure, modular, role-based HRM backend system that demonstrates real-world enterprise concepts and strengthened practical expertise in Java backend development, JDBC, database design, logging, and testing.
