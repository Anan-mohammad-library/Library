package org.example;

import org.example.domain.Admin;
import org.example.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminService();
    }


    @Test
    void testLoginSuccess() {
        assertTrue(adminService.login("anan", "1234"), "Login should succeed with valid credentials");
        assertTrue(adminService.isLoggedIn(), "Admin should be marked as logged in");
        assertNotNull(adminService.getCurrentAdmin(), "Current admin should not be null after successful login");
        assertEquals("anan", adminService.getCurrentAdmin().getUsername());
    }


    @Test
    void testLoginFailure() {
        assertFalse(adminService.login("wrong", "user"), "Login should fail for invalid credentials");
        assertFalse(adminService.isLoggedIn(), "System should not mark admin as logged in");
        assertNull(adminService.getCurrentAdmin(), "Current admin should be null after failed login");
    }


    @Test
    void testLogoutAfterLogin() {
        adminService.login("anan", "1234");
        adminService.logout();

        assertFalse(adminService.isLoggedIn(), "Admin should not be logged in after logout");
        assertNull(adminService.getCurrentAdmin(), "Current admin should be null after logout");
    }


    @Test
    void testLoginSwitchBetweenAdmins() {
        assertTrue(adminService.login("anan", "1234"));
        assertEquals("anan", adminService.getCurrentAdmin().getUsername());

        adminService.logout();
        assertTrue(adminService.login("mohammad", "123"));
        assertEquals("mohammad", adminService.getCurrentAdmin().getUsername());
    }


    @Test
    void testLoginWrongPassword() {
        assertFalse(adminService.login("anan", "wrong"));
        assertFalse(adminService.isLoggedIn());
    }


    @Test
    void testLoginWithNullValues() {
        assertFalse(adminService.login(null, null), "Login should fail if credentials are null");
        assertFalse(adminService.isLoggedIn());
    }


    @Test
    void testLoginWithEmptyStrings() {
        assertFalse(adminService.login("", ""), "Login should fail if credentials are empty");
        assertFalse(adminService.isLoggedIn());
    }


    @Test
    void testAddNewAdminAndLogin() {
        adminService.addAdmin("newadmin", "pass");
        assertTrue(adminService.login("newadmin", "pass"), "Should login successfully with newly added admin");
    }


    @Test
    void testGetAdminsNotEmpty() {
        assertNotNull(adminService.getAdmins(), "Admin list should not be null");
        assertFalse(adminService.getAdmins().isEmpty(), "Admin list should contain default admins");
    }

    @Test
    void testMultipleFailedLogins() {
        adminService.login("wrong1", "123");
        adminService.login("wrong2", "abc");
        assertFalse(adminService.isLoggedIn(), "Should not stay logged in after multiple failures");
        assertNull(adminService.getCurrentAdmin());
    }
}
