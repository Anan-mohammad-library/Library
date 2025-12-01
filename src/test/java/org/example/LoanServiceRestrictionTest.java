package org.example;

import org.example.domain.Loan;
import org.example.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceRestrictionTest {

    private LoanService loanService;

    @BeforeEach
    void setup() {
        new File("loans.txt").delete();
        loanService = new LoanService();
    }

    @Test
    void blocksBorrowWhenUserHasOverdue() {
        Loan l = new Loan("sam", "Some Book", "BOOK",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);
        l.checkOverdue();
        loanService.getAllLoans().add(l);

        loanService.borrowBook("sam", "Another Book");
        assertTrue(loanService.hasBlocks("sam"));
    }

    @Test
    void calculatesCDFineCorrectly() {
        Loan l = new Loan("moh", "Thriller", "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);
        l.checkOverdue();
        assertEquals(3 * 20, l.getFine());
    }

    @Test
    void blocksUserWithOverdueAfterBorrowAttempt() {
        Loan l = new Loan("tom", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        l.checkOverdue();
        loanService.getAllLoans().add(l);

        loanService.borrowBook("tom", "Book B");
        assertTrue(loanService.hasBlocks("tom"));
    }

    @Test
    void userWithoutOverdueHasNoBlocks() {
        loanService.borrowBook("nina", "Book X");
        assertFalse(loanService.hasBlocks("nina"));
    }

    @Test
    void payFineResetsUserBlocks() {
        Loan l = new Loan("kate", "Book Z", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(1).toString(),
                false, 0);
        l.checkOverdue();
        loanService.getAllLoans().add(l);
        loanService.borrowBook("kate", "Another Book");

        assertTrue(loanService.hasBlocks("kate"));
        loanService.payFine("kate");
        assertFalse(loanService.hasBlocks("kate"));
        assertEquals(0, l.getFine());
    }

    @Test
    void borrowBookWorksForMultipleUsers() {
        loanService.borrowBook("anna", "Book 1");
        loanService.borrowBook("bob", "Book 2");

        assertTrue(loanService.getAllLoans().stream().anyMatch(loan -> loan.getBorrower().equals("anna")));
        assertTrue(loanService.getAllLoans().stream().anyMatch(loan -> loan.getBorrower().equals("bob")));
    }

    @Test
    void overdueRefreshUpdatesFines() {
        Loan l = new Loan("mike", "Old Book", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loanService.getAllLoans().add(l);

        loanService.refreshOverdues();
        assertTrue(l.getFine() > 0);
        assertTrue(loanService.hasBlocks("mike"));
    }

    @Test
    void testShowAllLoansWhenEmpty() {
        new File("loans.txt").delete();
        loanService = new LoanService();
        assertTrue(loanService.getAllLoans().isEmpty());
        loanService.showAllLoans();
    }

    @Test
    void testShowUserLoansNoLoans() {
        loanService.showUserLoans("nobody");
    }

    @Test
    void testLoadLoansWhenFileMissing() {
        new File("loans.txt").delete();
        LoanService newService = new LoanService();
        assertTrue(newService.getAllLoans().isEmpty());
    }

    @Test
    void testBorrowSavesIntoFile() {
        loanService.borrowBook("adam", "Book A");
        File f = new File("loans.txt");
        assertTrue(f.exists());
    }

    @Test
    void testHasBlocksCaseInsensitive() {
        Loan l = new Loan("SAM", "Book", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        l.checkOverdue();
        loanService.getAllLoans().add(l);

        assertTrue(loanService.hasBlocks("sam"));
    }
}
