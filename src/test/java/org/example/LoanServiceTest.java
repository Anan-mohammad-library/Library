package org.example;

import org.example.domain.Loan;
import org.example.service.LoanService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {

    private LoanService loanService;

    @BeforeEach
    void setup() {
        new File("loans.txt").delete();
        loanService = new LoanService();
    }

    @Test
    void testBorrowBook() {
        loanService.borrowBook("ali", "Clean Code");
        assertEquals(1, loanService.getAllLoans().size());

        Loan loan = loanService.getAllLoans().get(0);
        assertEquals("ali", loan.getBorrower());
        assertEquals("Clean Code", loan.getItemTitle());
        assertEquals("BOOK", loan.getMediaType());
    }

    @Test
    void testBorrowCD() {
        loanService.borrowCD("ali", "CD Music");
        assertEquals(1, loanService.getAllLoans().size());

        Loan loan = loanService.getAllLoans().get(0);
        assertEquals("CD", loan.getMediaType());
    }

    @Test
    void testHasBlocksWhenOverdue() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);

        assertTrue(loanService.hasBlocks("ali"));
    }

    @Test
    void testHasBlocksNoOverdue() {
        Loan loan = new Loan("ali", "Book A", "BOOK");
        loanService.getAllLoans().add(loan);

        assertFalse(loanService.hasBlocks("ali"));
    }

    @Test
    void testBorrowBookBlocked() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);

        loan.checkOverdue();
        loanService.getAllLoans().add(loan);

        loanService.borrowBook("ali", "Another Book");
        assertEquals(1, loanService.getAllLoans().size());
    }

    @Test
    void testPayFine() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);

        assertTrue(loan.getFine() > 0);
        loanService.payFine("ali");
        assertEquals(0, loan.getFine());
        assertTrue(loan.isReturned());
    }

    @Test
    void testSaveAndLoadLoans() {
        loanService.borrowBook("ali", "Book A");

        LoanService newService = new LoanService();
        assertEquals(1, newService.getAllLoans().size());
    }

    @Test
    void testGetAllUsersWithOverdues() {
        Loan overdueLoan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        overdueLoan.checkOverdue();
        loanService.getAllLoans().add(overdueLoan);

        List<String> overdueUsers = loanService.getAllUsersWithOverdues();
        assertTrue(overdueUsers.contains("ali"));
    }

    @Test
    void testRefreshOverdues() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(15).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loanService.getAllLoans().add(loan);

        loanService.refreshOverdues();
        assertTrue(loan.getFine() > 0);
    }
}
