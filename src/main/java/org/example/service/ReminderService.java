package org.example.service;

import org.example.domain.Loan;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReminderService {

    private final Notifier notifier;
    private final LoanService loanService;
    private final UserService userService;
    private final EmailServer emailServer;
    private static final String LOG_FILE = "reminders.log";

    public ReminderService(Notifier notifier, LoanService loanService, UserService userService) {
        this(notifier, loanService, userService, new JakartaEmailServer());
    }

    public ReminderService(Notifier notifier, LoanService loanService, UserService userService, EmailServer emailServer) {
        this.notifier = notifier;
        this.loanService = loanService;
        this.userService = userService;
        this.emailServer = emailServer;
    }

    public void sendOverdueReminderTo(String userEmail) {
        loanService.refreshOverdues();
        List<Loan> overdueLoans = loanService.getAllLoans().stream()
                .filter(l -> !l.isReturned() && l.getFine() > 0)
                .toList();

        boolean sent = false;
        for (Loan loan : overdueLoans) {
            String borrowerEmail = userService.getEmail(loan.getBorrower());
            if (borrowerEmail != null && borrowerEmail.equalsIgnoreCase(userEmail)) {
                String msg = "You have overdue book(s). Please return them as soon as possible.";
                notifier.notify(userEmail, msg);
                try { emailServer.sendEmail(userEmail, msg); } catch (Exception ignored) {}
                saveReminder(userEmail, msg);
                sent = true;
            }
        }
        if (!sent) System.out.println("❌ Invalid email for user: " + userEmail + ", skipping.");
    }

    public void sendOverdueReminders() {
        loanService.refreshOverdues();
        List<Loan> overdueLoans = loanService.getAllLoans().stream()
                .filter(l -> !l.isReturned() && l.getFine() > 0)
                .toList();

        for (Loan loan : overdueLoans) {
            String borrowerEmail = userService.getEmail(loan.getBorrower());
            if (borrowerEmail != null) {
                String msg = "You have overdue book(s). Please return them as soon as possible.";
                notifier.notify(borrowerEmail, msg);
                try { emailServer.sendEmail(borrowerEmail, msg); } catch (Exception ignored) {}
                saveReminder(borrowerEmail, msg);
            }
        }
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
