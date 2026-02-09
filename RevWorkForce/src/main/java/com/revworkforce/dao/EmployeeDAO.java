package com.revworkforce.dao;

import com.revworkforce.model.EmployeeProfile;

import java.util.List;

/**EmployeeDAO defines database operations related to basic employee actions such as login and profile access.
 *These operations are common for all roles (EMPLOYEE, MANAGER, ADMIN).*/
public interface EmployeeDAO {

    boolean login(int empId, String password);//Validates employee login using ID and password.
    EmployeeProfile viewProfile(int empId);//Fetches profile details of a logged-in employee.
    Integer getManagerId(int empId);//Returns the manager ID for a given employee.
    boolean changePassword(int empId, String oldHashedPassword, String newHashedPassword);

    boolean updateProfile(int empId, String phone, String address, String emergencyContact);
    List<EmployeeProfile> getUpcomingBirthdays();

    List<EmployeeProfile> getWorkAnniversaries();
}
