package org.example;

import org.example.domain.Loan;
import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private static final String FILE_PATH = "books.txt";

    @BeforeEach
    void setup() throws IOException {
        // Prepare test book
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Test Book|Author X|123");
            writer.newLine();
        }
    }

    @AfterEach
    void cleanup() {
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();
    }

    @Test
    void testCreateLoanSetsDatesCorrectly() {
        Loan loan = new Loan("User1", "Test Book");

        assertEquals("User1", loan.getBorrower());
        assertEquals("Test Book", loan.getBookTitle());
        assertEquals(LocalDate.now(), loan.getBorrowDate());
        assertEquals(LocalDate.now().plusDays(28), loan.getDueDate());
        assertFalse(loan.isReturned());
        assertEquals(0, loan.getFine());
    }

    @Test
    void testLoanThrowsForNonexistentBook() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Loan("User1", "Nonexistent Book");
        });

        assertTrue(ex.getMessage().contains("does not exist"));
    }

    @Test
    void testCheckOverdueNoFineBeforeDueDate() {
        Loan loan = new Loan("User1", "Test Book");
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testCheckOverdueAfterDueDate() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(2).toString(),
                false, 0);

        loan.checkOverdue();
        assertEquals(2, loan.getFine(), "Fine should be 2 NIS (days late)");
    }

    @Test
    void testMarkReturnedResetsReturnedFlag() {
        Loan loan = new Loan("User1", "Test Book");
        loan.markReturned();
        assertTrue(loan.isReturned());
    }

    @Test
    void testPayFineResetsFineToZero() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(2).toString(),
                false, 0);

        loan.checkOverdue();
        assertTrue(loan.getFine() > 0);
        loan.payFine();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testToStringContainsAllInfo() {
        Loan loan = new Loan("User1", "Test Book");
        String str = loan.toString();
        assertTrue(str.contains("Test Book"));
        assertTrue(str.contains("User1"));
        assertTrue(str.contains("Due"));
        assertTrue(str.contains("Returned"));
        assertTrue(str.contains("Fine"));
    }

    @Test
    void testMultipleLoansSameUser() {
        Loan loan1 = new Loan("User1", "Test Book");
        Loan loan2 = new Loan("User1", "Test Book");

        assertEquals("User1", loan1.getBorrower());
        assertEquals("User1", loan2.getBorrower());
    }

    @Test
    void testReturnedBookNoFineEvenIfOverdue() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(2).toString(),
                false, 0);

        loan.markReturned();
        loan.checkOverdue();
        assertEquals(0, loan.getFine(), "Returned book should not accumulate fine");
    }

    @Test
    void testFineAccumulatesCorrectlyOverMultipleDays() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(35).toString(),
                LocalDate.now().minusDays(7).toString(),
                false, 0);

        loan.checkOverdue();
        assertEquals(7, loan.getFine(), "Fine should equal 7 NIS (days late)");
    }

    @Test
    void testPayFineAfterPartialOverdue() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(31).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);

        loan.checkOverdue();
        double fineBefore = loan.getFine();
        loan.payFine();
        assertEquals(0, loan.getFine(), "Fine should be cleared after paying");
        assertTrue(fineBefore > 0, "Fine should have existed before payment");
    }

    @Test
    void testLoanConstructorWithAllFields() {
        Loan loan = new Loan("User1", "Test Book",
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().plusDays(10).toString(),
                true, 5.0);

        assertEquals("User1", loan.getBorrower());
        assertEquals("Test Book", loan.getBookTitle());
        assertEquals(5.0, loan.getFine());
        assertTrue(loan.isReturned());
    }
}
