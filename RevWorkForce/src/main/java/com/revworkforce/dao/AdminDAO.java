package com.revworkforce.dao;

import com.revworkforce.model.Employee;
import java.util.List;

/** * AdminDAO defines database operations that can be performed only by ADMIN users.
 * This interface separates admin-specific database logic from service and controller layers. */
public interface AdminDAO {

    // Adds a new employee record to the system
    boolean addEmployee(Employee employee);

    // Updates basic employee details such as phone, address, department, etc.
    boolean updateEmployee(Employee employee);

    // Initializes default leave balance for a newly added employee
    boolean initializeLeaveBalance(int empId);

    // Activates or deactivates an employee account
    boolean updateEmployeeStatus(int empId, String status);

    // Changes or removes reporting manager for an employee
    boolean changeManager(int empId, Integer managerId);

    // Retrieves all employees in the organization
    List<Employee> getAllEmployees();

    // Searches employees by name, ID, or designation
    List<Employee> searchEmployees(String keyword);
}