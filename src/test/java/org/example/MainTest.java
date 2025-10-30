package org.example;
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
        assertTrue(adminService.login("anan", "1234"));
        assertTrue(adminService.isLoggedIn());
        assertNotNull(adminService.getCurrentAdmin());
        assertEquals("anan", adminService.getCurrentAdmin().getUsername());
    }


    @Test
    void testLoginFailure() {
        assertFalse(adminService.login("wrong", "user"));
        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
    }


    @Test
    void testLogoutAfterLogin() {
        adminService.login("anan", "1234");
        adminService.logout();

        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
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
        assertFalse(adminService.login(null, null));
        assertFalse(adminService.isLoggedIn());
    }


    @Test
    void testLoginWithEmptyStrings() {
        assertFalse(adminService.login("", ""));
        assertFalse(adminService.isLoggedIn());
    }


    @Test
    void testAddNewAdminAndLogin() {
        adminService.addAdmin("newadmin", "pass");
        assertTrue(adminService.login("newadmin", "pass"));
    }


    @Test
    void testGetAdminsNotEmpty() {
        assertNotNull(adminService.getAdmins());
        assertFalse(adminService.getAdmins().isEmpty());
    }

    @Test
    void testMultipleFailedLogins() {
        adminService.login("wrong1", "123");
        adminService.login("wrong2", "abc");
        assertFalse(adminService.isLoggedIn());
        assertNull(adminService.getCurrentAdmin());
    }
}
