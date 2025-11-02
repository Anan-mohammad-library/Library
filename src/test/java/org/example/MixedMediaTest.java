package org.example;

import org.example.domain.Loan;
import org.example.service.LoanService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MixedMediaTest {

    @BeforeEach
    void clean() {
        new File("loans.txt").delete();
    }

    @Test
    void testBookAndCDLoansTogether() {
        LoanService loanService = new LoanService();

        Loan b = new Loan("Omar", "Java Book", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(2).toString(),
                false, 0);

        Loan c = new Loan("Omar", "Thriller CD", "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);

        b.checkOverdue();
        c.checkOverdue();

        assertEquals(2, b.getFine());
        assertEquals(60, c.getFine());
        assertTrue(c.getFine() > b.getFine());
    }

    @Test
    void testMultipleLoansForSameUser() {
        Loan l1 = new Loan("Lina", "Book A", "BOOK");
        Loan l2 = new Loan("Lina", "CD B", "CD");
        l1.checkOverdue();
        l2.checkOverdue();

        assertEquals("Lina", l1.getBorrower());
        assertEquals("Lina", l2.getBorrower());
        assertEquals("BOOK", l1.getMediaType());
        assertEquals("CD", l2.getMediaType());
    }

    @Test
    void testOverdueCalculationForMixedMedia() {
        Loan bookLoan = new Loan("Sam", "Book X", "BOOK",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);

        Loan cdLoan = new Loan("Sam", "CD Y", "CD",
                LocalDate.now().minusDays(8).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);

        bookLoan.checkOverdue();
        cdLoan.checkOverdue();

        assertEquals(5, bookLoan.getFine());
        assertEquals(20*3, cdLoan.getFine());
    }

    @Test
    void testReturnedItemsNoFine() {
        Loan bookLoan = new Loan("Ali", "Book Z", "BOOK",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);

        Loan cdLoan = new Loan("Ali", "CD Z", "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);

        bookLoan.markReturned();
        cdLoan.markReturned();

        bookLoan.checkOverdue();
        cdLoan.checkOverdue();

        assertEquals(0, bookLoan.getFine());
        assertEquals(0, cdLoan.getFine());
    }
}
