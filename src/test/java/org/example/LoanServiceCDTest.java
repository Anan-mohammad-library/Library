package org.example;

import org.example.domain.Loan;
import org.example.service.CDService;
import org.example.service.LoanService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceCDTest {

    private LoanService loanService;
    private CDService cdService;

    @BeforeEach
    void setUp() {
        new File("loans.txt").delete();
        new File("cds.txt").delete();

        loanService = new LoanService();
        cdService = new CDService();

        cdService.addCD("Thriller", "Michael Jackson", "1");
        cdService.addCD("Bad", "Michael Jackson", "2");
    }

    @Test
    void testBorrowCD() {
        loanService.borrowCD("Ali", "Thriller");
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());

        Loan loan = loans.get(0);
        assertEquals("Ali", loan.getBorrower());
        assertEquals("Thriller", loan.getItemTitle());
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
        assertEquals("CD", loan.getMediaType());
        assertFalse(loan.isReturned());
        assertEquals(0, loan.getFine());
    }

    @Test
    void testMultipleBorrowCD() {
        loanService.borrowCD("Ali", "Thriller");
        loanService.borrowCD("Sara", "Bad");
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
    }

    @Test
    void testCDOverdueFine() {
        Loan loan = new Loan("Sara", "Thriller", "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);
        loan.checkOverdue();
        assertEquals(3 * 20, loan.getFine());
    }

    @Test
    void testPayFine() {
        Loan loan = new Loan("Sara", "Thriller", "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 60);
        loanService.getAllLoans().add(loan);
        loanService.payFine("Sara");
        assertEquals(0, loan.getFine());
    }

    @Test
    void testShowAllLoans() {
        loanService.borrowCD("Ali", "Thriller");
        loanService.borrowCD("Sara", "Bad");
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
    }

    @Test
    void testOverdueCalculation() {
        Loan loan = new Loan("John", "Bad", "CD",
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().minusDays(1).toString(),
                false, 0);
        loan.checkOverdue();
        assertEquals(1 * 20, loan.getFine());
    }



    @Test
    void testBorrowCDAlreadyBorrowed() {
        loanService.borrowCD("Ali", "Thriller");
        loanService.borrowCD("Sara", "Thriller");
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
    }
}
