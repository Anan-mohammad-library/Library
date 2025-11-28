package org.example;

import org.example.service.UserService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService();
    }

    @Test
    void testRegisterDuplicateUser() {
        userService.register("john", "pass123", "john@example.com");
        boolean result = userService.register("john", "pass456", "john2@example.com");
        assertFalse(result);
    }


    @Test
    void testLoginFailureWrongPassword() {
        userService.register("alice", "mypassword", "alice@example.com");
        assertFalse(userService.login("alice", "wrongpass"));
    }


    @Test
    void testLoginFailureUnknownUser() {
        assertFalse(userService.login("bob", "any"));
    }

    @Test
    void testGetEmailExistingUser() {
        userService.register("mike", "pass", "mike@example.com");
        assertEquals("mike@example.com", userService.getEmail("mike"));
    }

    @Test
    void testGetEmailUnknownUser() {
        assertNull(userService.getEmail("unknown"));
    }
}
