package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.dao.EmployeeDAOImpl;
import com.revworkforce.model.EmployeeProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * EmployeeService handles employee self-service operations.
 */
public class EmployeeService {

    private final EmployeeDAO dao = new EmployeeDAOImpl();
    private static final Logger logger =
            LogManager.getLogger(EmployeeService.class);

    public EmployeeProfile viewProfile(int empId) {
        logger.info("Employee profile viewed empId={}", empId);
        return dao.viewProfile(empId);
    }
    public boolean updateMyProfile(int empId, String phone, String address, String emergencyContact) {
        logger.info("Updating profile empId={}", empId);
        return dao.updateProfile(empId, phone, address, emergencyContact);
    }
    public List<EmployeeProfile> getUpcomingBirthdays() {
        return dao.getUpcomingBirthdays();
    }

    public List<EmployeeProfile> getWorkAnniversaries() {
        return dao.getWorkAnniversaries();
    }
}