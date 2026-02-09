package com.revworkforce.model;

import java.sql.Date;

/**
 * Employee model represents basic employee details.
 *
 * This class is used to transfer employee data
 * between UI, Service, and DAO layers.
 */
public class Employee {

    // ===== BASIC IDENTIFIERS =====
    private int empId;
    private String name;

    // ===== CONTACT DETAILS =====
    private String email;
    private String phone;
    private String address;

    // ===== PERSONAL & JOB DETAILS =====
    private Date dob;
    private String designation;
    private int departmentId;

    // ===== HIERARCHY & ACCESS =====
    private Integer managerId;   // Nullable (top-level employees)
    private int roleId;          // EMP / MANAGER / ADMIN

    // ===== AUTH & STATUS =====
    private String password;
    private String status;       // ACTIVE / INACTIVE

    // ===== GETTERS & SETTERS =====

    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getManagerId() {
        return managerId;
    }
    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public int getRoleId() {
        return roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}