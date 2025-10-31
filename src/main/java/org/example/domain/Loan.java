package org.example.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String borrower;
    private Media media;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private double fine;

    public Loan(String borrower, Media media) {
        this.borrower = borrower;
        this.media = media;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(media.getLoanDays());
        this.returned = false;
        this.fine = 0;
        media.setAvailable(false);
    }

    public void checkOverdue() {
        if (!returned && LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            fine = media.calculateFine(daysLate);
        } else fine = 0;
    }

    public void markReturned() {
        this.returned = true;
    }
    public void payFine() {
        fine = 0;
    }

    public String getBorrower() {
        return borrower;
    }
    public Media getMedia() {
        return media;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public boolean isReturned() {
        return returned;
    }
    public double getFine() {
        return fine;
    }

    @Override
    public String toString() {
        return String.format("Media: %s | Borrower: %s | Due: %s | Returned: %s | Fine: %.2f",
                media.getTitle(), borrower, dueDate, returned, fine);
    }
}