package org.example;

import org.example.service.AdminService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    @Test
    void testLoginSuccess() {
        AdminService admin = new AdminService();
        assertTrue(admin.login("anan", "1234"));
    }

    @Test
    void testLoginFailure() {
        AdminService admin = new AdminService();
        assertFalse(admin.login("wrong", "user"));
    }
}
