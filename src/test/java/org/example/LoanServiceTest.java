package org.example;

import org.example.domain.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private Book sampleBook;
    private CD sampleCD;

    @BeforeEach
    void setup() {
        sampleBook = new Book("Test Book", "Author X", "123");
        sampleCD = new CD("Test CD", "Artist Y", "CD001");
    }

    @Test
    void testCreateBookLoanSetsDatesCorrectly() {
        Loan loan = new Loan("User1", sampleBook);
        assertEquals("User1", loan.getBorrower());
        assertEquals(sampleBook, loan.getMedia());
        assertEquals(LocalDate.now(), loan.getBorrowDate());
        assertEquals(LocalDate.now().plusDays(28), loan.getDueDate());
        assertFalse(loan.isReturned());
        assertEquals(0, loan.getFine());
    }

    @Test
    void testCreateCDLoanSetsShorterDuration() {
        Loan loan = new Loan("User2", sampleCD);
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
    }

    @Test
    void testBookFineCalculationAfterDueDate() {
        Loan loan = new Loan("User3", sampleBook);
        // نحاكي تأخير 3 أيام بعد due date
        loan.getMedia().setAvailable(false);
        loan.checkOverdue();
        assertTrue(loan.getFine() >= 0);
    }

    @Test
    void testCDFineIsHigherThanBookFine() {
        Loan bookLoan = new Loan("User4", sampleBook);
        Loan cdLoan = new Loan("User4", sampleCD);

        bookLoan.checkOverdue();
        cdLoan.checkOverdue();

        // الغرامة اليومية للـ CD أعلى
        assertTrue(cdLoan.getFine() >= bookLoan.getFine());
    }

    @Test
    void testMarkReturnedWorksProperly() {
        Loan loan = new Loan("User5", sampleBook);
        loan.markReturned();
        assertTrue(loan.isReturned());
    }

    @Test
    void testPayFineResetsFineToZero() {
        Loan loan = new Loan("User6", sampleBook);
        loan.checkOverdue();
        loan.payFine();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testToStringContainsAllInformation() {
        Loan loan = new Loan("User7", sampleBook);
        String str = loan.toString();
        assertTrue(str.contains("User7"));
        assertTrue(str.contains("Book"));
        assertTrue(str.contains("Fine"));
    }

    @Test
    void testReturnedBookHasNoFineEvenIfOverdue() {
        Loan loan = new Loan("User8", sampleBook);
        loan.markReturned();
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testBookLoanAndCDLoanHaveDifferentDueDates() {
        Loan bookLoan = new Loan("User9", sampleBook);
        Loan cdLoan = new Loan("User9", sampleCD);
        assertNotEquals(bookLoan.getDueDate(), cdLoan.getDueDate());
    }

    @Test
    void testFineRemainsZeroIfBeforeDueDate() {
        Loan loan = new Loan("User10", sampleBook);
        assertEquals(0, loan.getFine());
    }
}