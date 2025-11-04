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
    private UserService fakeUserService;
    private File logFile;
    private FakeNotifier fakeNotifier;
    private FakeEmailServer fakeEmailServer;

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
        fakeUserService = new UserService();
        fakeUserService.register("bob", "pass", "bob@example.com");
        fakeUserService.register("alice", "pass", "alice@example.com");

        fakeNotifier = new FakeNotifier();
        fakeEmailServer = new FakeEmailServer();

        reminderService = new ReminderService(fakeNotifier, loanService, fakeUserService, fakeEmailServer);
        logFile = new File("reminders.log");

        Loan overdueLoan = new Loan("bob", "Clean Code", "BOOK",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);

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
        new File("reminders.log").delete();
    }

    @Test
    void testReminderServiceSendsForOverdueLoan() throws IOException {
        reminderService.sendOverdueReminderTo("bob@example.com");

        assertTrue(logFile.exists());
        List<String> lines = Files.readAllLines(Path.of("reminders.log"));
        assertTrue(lines.stream().anyMatch(l -> l.contains("bob@example.com")));
        assertTrue(fakeNotifier.users.contains("bob@example.com"));
        assertTrue(fakeEmailServer.emails.contains("bob@example.com"));
    }


    @Test
    void testEmailLogCreated() {
        reminderService.sendOverdueReminderTo("bob@example.com");
        assertTrue(logFile.exists());
    }

    @Test
    void testRefreshOverduesUpdatesFine() {
        loanService.refreshOverdues();
        boolean hasFine = loanService.getAllLoans().stream()
                .anyMatch(l -> l.getFine() > 0);
        assertTrue(hasFine);
    }
}

