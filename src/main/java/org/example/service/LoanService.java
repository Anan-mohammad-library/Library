package org.example.service;

import org.example.domain.Loan;
import java.io.*;
import java.util.*;

import static org.example.domain.Book.logger;

public class LoanService {

    private static final String FILE_PATH = "loans.txt";
    private List<Loan> loans;

    public LoanService() {
        loans = loadLoans();
    }

    public List<Loan> getAllLoans() {
        return loans;
    }

    public boolean hasBlocks(String borrower) {
        refreshOverdues();
        for (Loan l : loans) {
            if (l.getBorrower().equalsIgnoreCase(borrower)) {
                if (!l.isReturned() && l.getFine() > 0) return true;
                if (l.getFine() > 0) return true;
            }
        }
        return false;
    }

    public void borrowBook(String borrower, String bookTitle) {
        if (hasBlocks(borrower)) {
            logger.severe(" Borrow blocked: user has overdue items or unpaid fines.");
            return;
        }
        Loan loan = new Loan(borrower, bookTitle, "BOOK");
        loans.add(loan);
        saveLoans();
        logger.severe(borrower + " borrowed BOOK \"" + bookTitle + "\" until " + loan.getDueDate());
    }

    public void borrowCD(String borrower, String cdTitle) {
        if (hasBlocks(borrower)) {
            logger.severe(" Borrow blocked: user has overdue items or unpaid fines.");
            return;
        }
        Loan loan = new Loan(borrower, cdTitle, "CD");
        loans.add(loan);
        saveLoans();
        logger.severe(borrower + " borrowed CD \"" + cdTitle + "\" until " + loan.getDueDate());
    }

    public void showAllOverdues() {
        refreshOverdues();
        boolean found = false;

        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getFine() > 0) {
                System.out.println(loan);
                found = true;
            }
        }

        if (!found) logger.severe("ℹ No overdue items in the library.");
    }

    public void showUserOverdues(String borrower) {
        refreshOverdues();
        boolean found = false;

        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower) &&
                    !loan.isReturned() && loan.getFine() > 0) {
                System.out.println(loan);
                found = true;
            }
        }

        if (!found) logger.severe("ℹ You have no overdue items.");
    }

    public void refreshOverdues() {
        for (Loan loan : loans) {
            loan.checkOverdue();
        }
        saveLoans();
    }

    public void payFine(String borrower) {
        boolean paid = false;
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower) && loan.getFine() > 0) {
                loan.payFine();
                loan.markReturned();
                logger.severe(" Fine paid for " + borrower + " on: " + loan.getItemTitle());
                paid = true;
            }
        }
        if (!paid) logger.severe("ℹ No fines to pay for " + borrower);
        saveLoans();
    }

    private void saveLoans() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loans) {
                writer.write(loan.getBorrower() + "|" +
                        loan.getItemTitle() + "|" +
                        loan.getMediaType() + "|" +
                        loan.getBorrowDate() + "|" +
                        loan.getDueDate() + "|" +
                        loan.isReturned() + "|" +
                        loan.getFine());
                writer.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private List<Loan> loadLoans() {
        List<Loan> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 7) {
                    Loan loan = new Loan(p[0], p[1], p[2], p[3], p[4], Boolean.parseBoolean(p[5]), Double.parseDouble(p[6]));
                    list.add(loan);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public void showAllLoans() {
        if (loans.isEmpty()) {
            logger.severe("ℹ No loans in the system.");
            return;
        }
        for (Loan loan : loans) {
            System.out.println(loan);
        }
    }

    public void showUserLoans(String borrower) {
        boolean found = false;
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower)) {
                System.out.println(loan);
                found = true;
            }
        }
        if (!found) logger.severe("ℹ You have no loans.");
    }

    public List<String> getAllUsersWithOverdues() {
        refreshOverdues();
        Set<String> names = new HashSet<>();
        for (Loan loan : loans) {
            if (!loan.isReturned() && loan.getFine() > 0) {
                names.add(loan.getBorrower());
            }
        }
        return new ArrayList<>(names);
    }
}
