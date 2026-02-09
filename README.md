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

Presentation Layer (Console Menus)
        ↓
Service Layer (Business Logic & Validation)
        ↓
DAO Layer (JDBC Database Operations)
        ↓
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

Role-based menu access is enforced as follows:

EMPLOYEE → Employee Menu
MANAGER  → Employee + Manager Menu
ADMIN    → Employee + Manager + Admin Menu

This approach prevents unauthorized access and ensures hierarchy-based operations.

Application Workflows--
Login & Authentication Workflow:
User enters Employee ID and Password
        ↓
Password hashed using SHA-256
        ↓
Credentials validated from EMPLOYEE table
        ↓
Employee status check (ACTIVE / INACTIVE)
        ↓
Login Successful / Login Failed

Role-Based Access Control Workflow:
Login Success
        ↓
Fetch user role
        ↓
Display menus based on role

Employee Profile Management Workflow:
View Profile
        ↓
Service validation
        ↓
DAO fetches profile using joins
        ↓
Display non-sensitive employee details

Update Profile
        ↓
Validate ACTIVE status
        ↓
Update phone, address, emergency contact

Leave Management Workflow:
Employee applies for leave
        ↓
Initialize leave balance (if missing)
        ↓
Check overlapping leave dates
        ↓
Validate leave balance
        ↓
Manager exists?
        ↓
PENDING / Auto-APPROVED
        ↓
Update leave balance
        ↓
Create notification
        ↓
Create audit log

Notification Workflow:
System action occurs
        ↓
Notification created (UNREAD)
        ↓
User views notifications
        ↓
Notifications marked as READ

Audit Logging Workflow:
Critical action performed
        ↓
Audit entry created with timestamp

Goal Management Workflow:
Employee creates goal
        ↓
Track progress
        ↓
Manager views team goals
        ↓
Admin views all goals

Performance Review Workflow:
Employee submits self-review
        ↓
Status = SUBMITTED
        ↓
Manager adds feedback and rating
        ↓
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
RevWorkForce
│── src/main/java
│   ├── model
│   ├── dao
│   ├── service
│   ├── util
│   └── main
│
│── src/test/java
│   └── service tests
│
│── resources
│   └── log4j2.xml
│
│── pom.xml

How to Run the Project:
Clone the repository
Configure Oracle DB connection in DBUtil
Execute SQL scripts for tables and sequences
Run the Main class
Log in using valid employee credentials


Project Summary:
RevWorkForce is a secure, modular, role-based HRM backend system that demonstrates real-world enterprise concepts and strengthened practical expertise in Java backend development, JDBC, database design, logging, and testing.
