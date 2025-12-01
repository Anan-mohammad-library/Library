package org.example;

import org.example.domain.Loan;
import org.example.service.UserService;
import org.example.service.LoanService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setup() {
        new File("users.txt").delete();
        new File("loans.txt").delete();

        userService = new UserService();
        loanService = new LoanService();
    }

    @AfterEach
    void cleanup() {
        new File("users.txt").delete();
        new File("loans.txt").delete();
    }

    @Test
    void testRegisterNewUser() {
        boolean result = userService.register("john", "pass123", "john@example.com");
        assertTrue(result);
        assertTrue(userService.login("john", "pass123"));
    }

    @Test
    void testRegisterDuplicateUser() {
        userService.register("john", "pass123", "john@example.com");
        boolean result = userService.register("john", "pass456", "john2@example.com");
        assertFalse(result);
    }

    @Test
    void testLoginSuccess() {
        userService.register("alice", "mypassword", "alice@example.com");
        assertTrue(userService.login("alice", "mypassword"));
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

    @Test
    void testCaseInsensitiveLogin() {
        userService.register("Mark", "123", "mark@example.com");
        assertTrue(userService.login("mark", "123"));
        assertTrue(userService.login("MARK", "123"));
    }

    @Test
    void testCaseInsensitiveGetEmail() {
        userService.register("Sam", "pass", "sam@example.com");
        assertEquals("sam@example.com", userService.getEmail("SAM"));
        assertEquals("sam@example.com", userService.getEmail("sam"));
    }

    @Test
    void testUserRemovedIfNoLoans() {
        userService.register("alex", "pass", "alex@example.com");
        userService.unregister("alex", loanService);
        assertFalse(userService.login("alex", "pass"));
        assertNull(userService.getEmail("alex"));
    }

    @Test
    void testUserNotRemovedIfHasLoan() {
        userService.register("bob", "pass", "bob@example.com");

        Loan loan = new Loan("bob", "Book A", "BOOK");
        loanService.getAllLoans().add(loan);

        userService.unregister("bob", loanService);

        assertTrue(userService.login("bob", "pass"));
        assertEquals("bob@example.com", userService.getEmail("bob"));
    }

    @Test
    void testUserNotRemovedIfHasFine() {
        userService.register("kate", "pass", "kate@example.com");

        Loan loan = new Loan("kate", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);

        userService.unregister("kate", loanService);

        assertTrue(userService.login("kate", "pass"));
    }

    @Test
    void testUsersPersistedToFile() {
        userService.register("danny", "pass", "d@e.com");

        UserService newService = new UserService();
        assertTrue(newService.login("danny", "pass"));
        assertEquals("d@e.com", newService.getEmail("danny"));
    }
}
