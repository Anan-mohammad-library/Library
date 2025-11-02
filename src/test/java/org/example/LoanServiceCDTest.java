package org.example;

import org.example.domain.Loan;
import org.example.service.CDService;
import org.example.service.LoanService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

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
    }

    @Test
    void testBorrowCD() {
        loanService.borrowCD("Ali", "Thriller");

        assertFalse(loanService.getAllLoans().isEmpty());
        Loan loan = loanService.getAllLoans().get(0);

        assertEquals("Ali", loan.getBorrower());
        assertEquals("Thriller", loan.getItemTitle());
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
        assertEquals("CD", loan.getMediaType());
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
}
