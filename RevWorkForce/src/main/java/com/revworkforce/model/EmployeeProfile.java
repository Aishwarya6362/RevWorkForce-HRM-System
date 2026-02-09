package com.revworkforce.model;

import java.util.Date;

public class EmployeeProfile {

    private int empId;
    private String name;
    private String email;
    private String phone;
    private Date dob;
    private String roleName;
    private String departmentName;
    private Integer managerId;
    private String managerName;
    private String address;
    private String emergencyContact;
    private int yearsCompleted;


    // getters & setters
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) {this.address = address; }

    public String getEmergencyContact() { return emergencyContact;}
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact;}

    public int getYearsCompleted() { return yearsCompleted; }
    public void setYearsCompleted(int yearsCompleted) { this.yearsCompleted = yearsCompleted; }

    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

}