package org.example;

import org.example.domain.Loan;
import org.example.service.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReminderServiceTest {

    private static final String EMAIL_LOG = "emails.log";

    @BeforeEach
    void setup() throws IOException {

        File f = new File(EMAIL_LOG);
        if (f.exists()) new FileWriter(f, false).close();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("loans.txt"))) {
            writer.write("bob|Clean Code|" +
                    LocalDate.now().minusDays(30) + "|" +
                    LocalDate.now().minusDays(2) + "|false|0");
            writer.newLine();
        }
    }

    @Test
    void testReminderSentToOverdueUser() throws IOException {
        LoanService loanService = new LoanService();
        ReminderService reminderService =
                new ReminderService(new EmailNotifier(new FakeEmailServer()), loanService);

        reminderService.sendOverdueReminders();


        List<String> lines = java.nio.file.Files.readAllLines(new File(EMAIL_LOG).toPath());
        assertFalse(lines.isEmpty(), "Email log should contain reminder");
        assertTrue(lines.get(0).contains("bob"), "Email should be sent to bob");
    }

    @AfterEach
    void cleanup() {
        new File("loans.txt").delete();
        new File(EMAIL_LOG).delete();
    }
}
