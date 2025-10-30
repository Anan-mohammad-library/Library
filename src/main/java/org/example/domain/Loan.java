package org.example.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String borrower;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private double fine;


    public Loan(String borrower, String bookTitle) {
        if (!isBookAvailable(bookTitle)) {
            throw new IllegalArgumentException("❌ The book \"" + bookTitle + "\" does not exist in the library file.");
        }

        this.borrower = borrower;
        this.bookTitle = bookTitle;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(28);
        this.returned = false;
        this.fine = 0;
    }


    public Loan(String borrower, String bookTitle, String borrowDate, String dueDate, boolean returned, double fine) {
        this.borrower = borrower;
        this.bookTitle = bookTitle;
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
            fine = daysLate * 1.0; // 1 NIS per day
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

    public String getBorrower() {
        return borrower;
    }

    public String getBookTitle() {
        return bookTitle;
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
        return String.format("Book: %s | Borrower: %s | Due: %s | Returned: %s | Fine: %.2f",
                bookTitle, borrower, dueDate, returned, fine);
    }
}
