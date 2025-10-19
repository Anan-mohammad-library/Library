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

    public void borrowBook(String borrower, String bookTitle) {
        Loan loan = new Loan(borrower, bookTitle);
        loans.add(loan);
        saveLoans();
        System.out.println("✅ " + borrower + " borrowed \"" + bookTitle + "\" until " + loan.getDueDate());
    }

    public void checkOverdueBooks() {
        boolean found = false;
        for (Loan loan : loans) {
            loan.checkOverdue();
            if (loan.getFine() > 0) {
                System.out.println("⚠️ Overdue: " + loan);
                found = true;
            }
        }
        if (!found)
            System.out.println("✅ No overdue books.");
    }

    public void payFine(String borrower) {
        boolean paid = false;
        for (Loan loan : loans) {
            if (loan.getBorrower().equalsIgnoreCase(borrower) && loan.getFine() > 0) {
                loan.payFine();
                loan.markReturned();
                System.out.println("💰 Fine paid successfully for " + borrower);
                paid = true;
            }
        }
        if (!paid)
            System.out.println("ℹ️ No fines to pay for " + borrower);
        saveLoans();
    }

    // ================= File Save/Load =================
    private void saveLoans() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loans) {
                writer.write(loan.getBorrower() + "|" + loan.getBookTitle() + "|" +
                        loan.getBorrowDate() + "|" + loan.getDueDate() + "|" +
                        loan.isReturned() + "|" + loan.getFine());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                if (parts.length >= 6) {
                    Loan loan = new Loan(parts[0], parts[1], parts[2], parts[3],
                            Boolean.parseBoolean(parts[4]),
                            Double.parseDouble(parts[5]));

                    list.add(loan);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
