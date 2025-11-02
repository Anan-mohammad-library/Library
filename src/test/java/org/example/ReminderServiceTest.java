package org.example;

import org.example.domain.Loan;
import org.example.service.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReminderServiceTest {

    private LoanService loanService;
    private ReminderService reminderService;
    private File emailLog;

    @BeforeEach
    void setup() {
        new File("loans.txt").delete();
        new File("emails.log").delete();

        loanService = new LoanService();
        reminderService = new ReminderService(
                new EmailNotifier(new FakeEmailServer()), loanService
        );

        emailLog = new File("emails.log");

        // overdue BOOK loan
        Loan overdueLoan = new Loan("bob", "Clean Code", "BOOK",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);

        // normal BOOK loan
        Loan normalLoan = new Loan("alice", "Java Concurrency", "BOOK",
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().plusDays(10).toString(),
                false, 0);

        overdueLoan.checkOverdue();
        normalLoan.checkOverdue();

        loanService.getAllLoans().add(overdueLoan);
        loanService.getAllLoans().add(normalLoan);

        loanService.refreshOverdues();
    }

    @AfterEach
    void cleanup() {
        new File("loans.txt").delete();
        new File("emails.log").delete();
    }

    @Test
    void testReminderServiceSendsForOverdueLoan() throws IOException {
        reminderService.sendOverdueReminders();

        assertTrue(emailLog.exists());
        assertTrue(emailLog.length() > 0);

        List<String> lines = Files.readAllLines(Path.of("emails.log"));
        assertTrue(lines.stream().anyMatch(l -> l.contains("bob")));
    }

    @Test
    void testNonOverdueUserNotInEmailLog() throws IOException {
        reminderService.sendOverdueReminders();

        List<String> lines = Files.readAllLines(Path.of("emails.log"));
        assertFalse(lines.stream().anyMatch(l -> l.contains("alice")));
    }

    @Test
    void testEmailLogCreated() {
        reminderService.sendOverdueReminders();
        assertTrue(emailLog.exists());
    }

    @Test
    void testRefreshOverduesUpdatesFine() {
        loanService.refreshOverdues();
        boolean hasFine = loanService.getAllLoans().stream()
                .anyMatch(l -> l.getFine() > 0);
        assertTrue(hasFine);
    }

    @Test
    void testReturnedLoanDoesNotTriggerEmail() throws IOException {
        Loan returned = new Loan("omar", "Refactoring", "BOOK",
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().minusDays(2).toString(),
                false, 0);

        returned.markReturned();
        loanService.getAllLoans().add(returned);

        reminderService.sendOverdueReminders();
        List<String> lines = Files.readAllLines(Path.of("emails.log"));

        assertFalse(lines.stream().anyMatch(l -> l.contains("omar")));
    }
}
