package org.example.service;
import org.example.domain.Loan;
import java.util.*;
import java.util.stream.Collectors;
public class ReminderService {

    private final Notifier notifier;
    private final LoanService loanService;

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

        overdueCount.forEach((user, count) -> {
            String msg = "You have " + count + " overdue book(s).";
            notifier.notify(user, msg);
            System.out.println("Reminder sent to " + user + ": " + msg);
        });
    }
}
