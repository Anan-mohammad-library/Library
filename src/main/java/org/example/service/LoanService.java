package org.example.service;

import org.example.domain.Loan;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoanService {

    private static final String FILE_PATH = "loans.txt";
    private List<Loan> loans;

    public LoanService() {
        loans = loadLoans();
    }

    // Recalculate fines for all
    public void refreshOverdues() {
        for (Loan loan : loans) {
            loan.checkOverdue();
        }
        saveLoans();
    }

    // Block user if has overdue or unpaid fines
    public boolean hasBlocks(String borrower) {
        refreshOverdues();
        for (Loan l : loans) {
            if (l.getBorrower().equalsIgnoreCase(borrower)) {
                if (!l.isReturned() && l.getFine() > 0) return true; // overdue
                if (l.getFine() > 0) return true; // unpaid fine
            }
        }
        return false;
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public List<Loan> getLoansByBorrower(String borrower) {
        List<Loan> out = new ArrayList<>();
        for (Loan l : loans) {
            if (l.getBorrower().equalsIgnoreCase(borrower)) out.add(l);
        }
        return out;
    }

    // Borrow BOOK (default)
    public void borrowBook(String borrower, String bookTitle) {
        if (hasBlocks(borrower)) {
            System.out.println("❌ Borrow blocked: user has overdue books or unpaid fines.");
            return;
        }

        Loan loan = new Loan(borrower, bookTitle, "BOOK");
        loans.add(loan);
        saveLoans();
        System.out.println(borrower + " borrowed BOOK \"" + bookTitle + "\" until " + loan.getDueDate());
    }

    // ✅ Borrow CD
    public void borrowCD(String borrower, String cdTitle) {
        if (hasBlocks(borrower)) {
            System.out.println("❌ Borrow blocked: user has overdue CDs/books or unpaid fines.");
            return;
        }

        Loan loan = new Loan(borrower, cdTitle, "CD");
        loans.add(loan);
        saveLoans();
        System.out.println(borrower + " borrowed CD \"" + cdTitle + "\" until " + loan.getDueDate());
    }

    // Check overdue loans
    public void checkOverdueBooks() {
        boolean found = false;
        for (Loan loan : loans) {
            loan.checkOverdue();
            if (!loan.isReturned() && loan.getFine() > 0) {
                System.out.println(" Overdue: " + loan);
                found = true;
            }
        }
        if (!found) System.out.println(" No overdue items.");
        saveLoans();
    }

    // Pay all fines for user
    public void payFine(String borrower) {
        boolean paid = false;
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower) && loan.getFine() > 0) {
                loan.payFine();
                loan.markReturned();
                System.out.println("✅ Fine paid for " + borrower + " on: " + loan.getItemTitle());
                paid = true;
            }
        }
        if (!paid)
            System.out.println("ℹ No fines to pay for " + borrower);
        saveLoans();
    }

    // Save loans to file including mediaType
    private void saveLoans() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loans) {
                writer.write(
                        loan.getBorrower() + "|" +
                                loan.getItemTitle() + "|" +
                                loan.getMediaType() + "|" +
                                loan.getBorrowDate() + "|" +
                                loan.getDueDate() + "|" +
                                loan.isReturned() + "|" +
                                loan.getFine()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load from file including mediaType
    private List<Loan> loadLoans() {
        List<Loan> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 7) {
                    Loan loan = new Loan(
                            p[0],  // borrower
                            p[1],  // itemTitle
                            p[2],  // mediaType
                            p[3],  // borrowDate
                            p[4],  // dueDate
                            Boolean.parseBoolean(p[5]), // returned
                            Double.parseDouble(p[6])    // fine
                    );
                    list.add(loan);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
