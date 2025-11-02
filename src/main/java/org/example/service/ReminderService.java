package org.example.service;

import org.example.domain.Loan;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReminderService {

    private final Notifier notifier;
    private final LoanService loanService;
    private static final String LOG_FILE = "reminders.log";

    public ReminderService(Notifier notifier, LoanService loanService) {
        this.notifier = notifier;
        this.loanService = loanService;
    }

    public void sendOverdueReminders() {
        loanService.refreshOverdues();

        Map<String, Long> overdueCount = loanService.getAllLoans().stream()
                .filter(l -> !l.isReturned() && l.getFine() > 0)
                .collect(Collectors.groupingBy(Loan::getBorrower, Collectors.counting()));

        if (overdueCount.isEmpty()) {
            System.out.println("No overdue users to notify.");
            return;
        }

        int totalMessages = 0;
        System.out.println(" Sending reminders...");

        for (Map.Entry<String, Long> entry : overdueCount.entrySet()) {
            String user = entry.getKey();
            long count = entry.getValue();
            String msg = "You have " + count + " overdue book(s).";

            notifier.notify(user, msg);  // إرسال التذكير
            System.out.println(" Reminder sent to " + user + ": " + msg);


            saveReminder(user, msg);

            totalMessages++;
        }

        System.out.println(" Total reminders sent: " + totalMessages);
    }

    private void saveReminder(String user, String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write("TO: " + user + " | MSG: " + message);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing reminder log: " + e.getMessage());
        }
    }
}
