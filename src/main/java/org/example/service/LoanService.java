package org.example.service;

import org.example.domain.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoanService {

    private static final String FILE_PATH = "loans.txt";
    private final List<Loan> loans;

    public LoanService() {
        loans = loadLoans();
    }


    public List<Loan> getAllLoans() {
        return loans;
    }


    public List<Loan> getLoansByBorrower(String borrower) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower)) {
                result.add(loan);
            }
        }
        return result;
    }


    public void refreshOverdues() {
        for (Loan loan : loans) loan.checkOverdue();
        saveLoans();
    }


    public boolean hasBlocks(String borrower) {
        refreshOverdues();
        for (Loan l : loans) {
            if (l.getBorrower().equalsIgnoreCase(borrower)
                    && !l.isReturned()
                    && l.getFine() > 0) {
                return true;
            }
        }
        return false;
    }


    public void borrowBook(String borrower, String bookTitle) {
        if (hasBlocks(borrower)) {
            System.out.println("❌ Borrow blocked: user has overdue or unpaid fines.");
            return;
        }

        Book book = Book.findBookByTitle(bookTitle);
        if (book == null) {
            System.out.println("❌ Book not found: " + bookTitle);
            return;
        }

        Loan loan = new Loan(borrower, book);
        loans.add(loan);
        saveLoans();
        System.out.println("✅ " + borrower + " borrowed book \"" + bookTitle + "\" until " + loan.getDueDate());
    }


    public void borrowCD(String borrower, String cdTitle) {
        if (hasBlocks(borrower)) {
            System.out.println("❌ Borrow blocked: user has overdue or unpaid fines.");
            return;
        }

        CD cd = CD.findCDByTitle(cdTitle);
        if (cd == null) {
            System.out.println("❌ CD not found: " + cdTitle);
            return;
        }

        Loan loan = new Loan(borrower, cd);
        loans.add(loan);
        saveLoans();
        System.out.println("✅ " + borrower + " borrowed CD \"" + cdTitle + "\" until " + loan.getDueDate());
    }


    public void checkOverdueMedia() {
        boolean found = false;
        for (Loan loan : loans) {
            loan.checkOverdue();
            if (loan.getFine() > 0 && !loan.isReturned()) {
                System.out.println("⚠ Overdue: " + loan);
                found = true;
            }
        }
        if (!found) System.out.println("✅ No overdue media found.");
        saveLoans();
    }


    public void payFine(String borrower) {
        boolean paid = false;
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower) && loan.getFine() > 0) {
                loan.payFine();
                loan.markReturned();
                System.out.println("✅ Fine paid for " + borrower + " on: " + loan.getMedia().getTitle());
                paid = true;
            }
        }
        if (!paid) System.out.println("ℹ No fines to pay for " + borrower);
        saveLoans();
    }


    private void saveLoans() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loans) {
                writer.write(
                        loan.getBorrower() + "|" +
                                loan.getMedia().getTitle() + "|" +
                                loan.getBorrowDate() + "|" +
                                loan.getDueDate() + "|" +
                                loan.isReturned() + "|" +
                                loan.getFine() + "|" +
                                loan.getMedia().getClass().getSimpleName()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Error saving loans: " + e.getMessage());
        }
    }


    private List<Loan> loadLoans() {
        List<Loan> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String borrower = parts[0].trim();
                    String title = parts[1].trim();
                    String type = parts[6].trim();

                    Media media = type.equalsIgnoreCase("CD")
                            ? new CD(title, "Unknown", "N/A")
                            : new Book(title, "Unknown", "N/A");

                    Loan loan = new Loan(borrower, media);
                    list.add(loan);
                }
            }
        } catch (IOException e) {
            System.out.println(" Error loading loans: " + e.getMessage());
        }
        return list;
    }
}