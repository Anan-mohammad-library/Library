package org.example.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String borrower;
    private String itemTitle;
    private String mediaType;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private double fine;


    public Loan(String borrower, String itemTitle) {
        this(borrower, itemTitle, "BOOK");


        if (!isBookAvailable(itemTitle)) {
            throw new IllegalArgumentException(" The book \"" + itemTitle + "\" does not exist in the library file.");
        }
    }

    public Loan(String borrower, String itemTitle, String mediaType) {
        this.borrower = borrower;
        this.itemTitle = itemTitle;
        this.mediaType = mediaType.toUpperCase();
        this.borrowDate = LocalDate.now();

        if (mediaType.equalsIgnoreCase("CD")) {
            this.dueDate = borrowDate.plusDays(7);
        } else {
            this.dueDate = borrowDate.plusDays(28);
        }

        this.returned = false;
        this.fine = 0;
    }


    public Loan(String borrower, String itemTitle, String mediaType,
                String borrowDate, String dueDate, boolean returned, double fine) {

        this.borrower = borrower;
        this.itemTitle = itemTitle;
        this.mediaType = mediaType;
        this.borrowDate = LocalDate.parse(borrowDate);
        this.dueDate = LocalDate.parse(dueDate);
        this.returned = returned;
        this.fine = fine;
    }

    private boolean isBookAvailable(String title) {
        List<Book> books = Book.loadBooksFromFile();
        return books.stream().anyMatch(b -> b.getTitle().equalsIgnoreCase(title));
    }

    public void checkOverdue() {
        if (!returned && LocalDate.now().isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, LocalDate.now());

            if (mediaType.equals("CD")) {
                fine = daysLate * 20;
            } else {
                fine = daysLate * 1;
            }
        } else {
            fine = 0;
        }
    }

    public void markReturned() {
        this.returned = true;
    }

    public void payFine() {
        fine = 0;
    }

    public String getBorrower() { return borrower; }

    public String getItemTitle() { return itemTitle; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public LocalDate getDueDate() { return dueDate; }

    public boolean isReturned() { return returned; }

    public double getFine() { return fine; }

    public String getMediaType() { return mediaType; }

    @Override
    public String toString() {
        return String.format(
                "%s: %s | Borrower: %s | Due: %s | Returned: %s | Fine: %.2f",
                mediaType, itemTitle, borrower, dueDate, returned, fine
        );
    }
}
