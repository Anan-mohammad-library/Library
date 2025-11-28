package org.example;

import org.example.domain.Loan;
import org.example.service.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReminderServiceTest {

    private LoanService loanService;
    private ReminderService reminderService;
    private UserService userService;
    private FakeNotifier fakeNotifier;
    private FakeEmailServer fakeEmailServer;
    private File logFile;

    static class FakeNotifier implements Notifier {
        List<String> users = new java.util.ArrayList<>();
        List<String> messages = new java.util.ArrayList<>();
        @Override
        public void notify(String user, String message) {
            users.add(user);
            messages.add(message);
        }
    }

    static class FakeEmailServer implements EmailServer {
        List<String> emails = new java.util.ArrayList<>();
        List<String> messages = new java.util.ArrayList<>();
        @Override
        public void sendEmail(String to, String message) {
            emails.add(to);
            messages.add(message);
        }
    }

    @BeforeEach
    void setup() {
        new File("loans.txt").delete();
        new File("reminders.log").delete();

        loanService = new LoanService();
        userService = new UserService();
        userService.register("bob", "pass", "bob@example.com");
        userService.register("alice", "pass", "alice@example.com");
        userService.register("tom", "pass", "tom@example.com");

        fakeNotifier = new FakeNotifier();
        fakeEmailServer = new FakeEmailServer();

        reminderService = new ReminderService(fakeNotifier, loanService, userService, fakeEmailServer);
        logFile = new File("reminders.log");

        // Loan متأخر
        Loan overdueLoan = new Loan("bob", "Clean Code", "BOOK",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(), false, 0);
        overdueLoan.checkOverdue();
        loanService.getAllLoans().add(overdueLoan);

        // Loan غير متأخر
        Loan normalLoan = new Loan("alice", "Java Concurrency", "BOOK",
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().plusDays(10).toString(), false, 0);
        normalLoan.checkOverdue();
        loanService.getAllLoans().add(normalLoan);

        loanService.refreshOverdues();
    }

    @AfterEach
    void cleanup() {
        new File("loans.txt").delete();
        new File("reminders.log").delete();
    }

    @Test
    void testReminderSentForOverdue() throws IOException {
        reminderService.sendOverdueReminderTo("bob@example.com");
        assertTrue(fakeNotifier.users.contains("bob@example.com"));
        assertTrue(fakeEmailServer.emails.contains("bob@example.com"));

        List<String> lines = Files.readAllLines(Path.of("reminders.log"));
        assertTrue(lines.stream().anyMatch(l -> l.contains("bob@example.com")));
    }

    @Test
    void testNoReminderForUserWithoutOverdue() throws IOException {
        reminderService.sendOverdueReminderTo("alice@example.com");
        assertFalse(fakeNotifier.users.contains("alice@example.com"));
        assertFalse(fakeEmailServer.emails.contains("alice@example.com"));
        assertFalse(logFile.exists());
    }

    @Test
    void testMultipleOverduesForSameUser() {
        Loan l1 = new Loan("tom", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(), false, 0);
        Loan l2 = new Loan("tom", "Book B", "BOOK",
                LocalDate.now().minusDays(15).toString(),
                LocalDate.now().minusDays(1).toString(), false, 0);
        l1.checkOverdue(); l2.checkOverdue();
        loanService.getAllLoans().add(l1); loanService.getAllLoans().add(l2);
        loanService.refreshOverdues();

        assertEquals(2, loanService.getAllLoans().stream()
                .filter(l -> l.getBorrower().equals("tom") && l.getFine() > 0).count());
    }

    @Test
    void testReminderForNonExistingUserDoesNothing() throws IOException {
        reminderService.sendOverdueReminderTo("unknown@example.com");
        assertTrue(fakeNotifier.users.isEmpty());
        assertTrue(fakeEmailServer.emails.isEmpty());
        assertFalse(logFile.exists());
    }

    @Test
    void testMarkReturnedResetsFine() {
        Loan loan = new Loan("bob", "Book Z", "BOOK",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(5).toString(), false, 0);
        loan.checkOverdue();
        assertTrue(loan.getFine() > 0);

        loan.markReturned();
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testRefreshOverduesUpdatesFines() {
        loanService.refreshOverdues();
        boolean anyFine = loanService.getAllLoans().stream().anyMatch(l -> l.getFine() > 0);
        assertTrue(anyFine);
    }
    @Test
    void testMultipleLoansSendReminder() throws IOException {
        Loan l1 = new Loan("bob", "Book 1", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(), false, 0);
        Loan l2 = new Loan("bob", "Book 2", "CD",
                LocalDate.now().minusDays(15).toString(),
                LocalDate.now().minusDays(3).toString(), false, 0);
        l1.checkOverdue(); l2.checkOverdue();
        loanService.getAllLoans().add(l1); loanService.getAllLoans().add(l2);

        loanService.refreshOverdues();
        reminderService.sendOverdueReminderTo("bob@example.com");

        assertTrue(fakeNotifier.users.contains("bob@example.com"));
        assertTrue(fakeEmailServer.emails.contains("bob@example.com"));
    }

    @Test
    void testUserWithoutLoanNoReminder() {
        reminderService.sendOverdueReminderTo("alice@example.com"); // alice ليس لديها قرض متأخر
        assertFalse(fakeNotifier.users.contains("alice@example.com"));
        assertFalse(fakeEmailServer.emails.contains("alice@example.com"));
    }
    @Test
    void testRefreshOverduesMultipleLoans() {
        Loan l1 = new Loan("tom", "Book A", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(1).toString(), false, 0);
        Loan l2 = new Loan("tom", "Book B", "CD",
                LocalDate.now().minusDays(25).toString(),
                LocalDate.now().minusDays(5).toString(), false, 0);
        l1.checkOverdue(); l2.checkOverdue();
        loanService.getAllLoans().add(l1); loanService.getAllLoans().add(l2);

        loanService.refreshOverdues();

        assertTrue(loanService.getAllLoans().stream().anyMatch(l -> l.getFine() > 0));
    }

}
