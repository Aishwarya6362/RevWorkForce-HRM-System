package com.revworkforce.test;

import com.revworkforce.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

//    @Test
//    void testValidLogin() {
//        assertTrue(authService.login(1001, "admin123"));
//    }

    @Test
    void testInvalidLogin() {
        assertFalse(authService.login(9999, "wrong"));
    }
}