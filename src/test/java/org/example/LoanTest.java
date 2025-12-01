package org.example;

import org.example.domain.Book;
import org.example.domain.Loan;
import org.junit.jupiter.api.*;

import java.io.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private static final String BOOKS_FILE = "books.txt";

    @BeforeEach
    void setup() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            writer.write("Test Book|Author X|123");
            writer.newLine();
            writer.write("Another Book|Someone|999");
            writer.newLine();
        }
    }

    @AfterEach
    void cleanup() {
        new File(BOOKS_FILE).delete();
    }

    @Test
    void testLoanConstructorBookExists() {
        Loan loan = new Loan("User1", "Test Book");
        assertEquals("User1", loan.getBorrower());
        assertEquals("Test Book", loan.getItemTitle());
        assertEquals("BOOK", loan.getMediaType());
        assertEquals(LocalDate.now(), loan.getBorrowDate());
        assertEquals(LocalDate.now().plusDays(28), loan.getDueDate());
    }

    @Test
    void testLoanConstructorBookDoesNotExistThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Loan("User2", "Nonexistent Book");
        });
    }

    @Test
    void testLoanConstructorWithMediaTypeCD() {
        Loan loan = new Loan("Ali", "Thriller", "CD");
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
    }

    @Test
    void testLoanConstructorWithMediaTypeBookExplicit() {
        Loan loan = new Loan("Ali", "Test Book", "BOOK");
        assertEquals(LocalDate.now().plusDays(28), loan.getDueDate());
    }

    @Test
    void testCheckOverdueBOOK() {
        Loan loan = new Loan("User", "Test Book", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);

        loan.checkOverdue();
        assertEquals(5, loan.getFine());   // 5 days * 1
    }

    @Test
    void testCheckOverdueCD() {
        Loan loan = new Loan("User", "Test Book", "CD",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(3).toString(),
                false, 0);
        loan.checkOverdue();
        assertEquals(60, loan.getFine());  // 3 * 20
    }

    @Test
    void testCheckOverdueBeforeDueDate() {
        Loan loan = new Loan("User", "Test Book", "BOOK");
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testReturnedLoanNeverGetsFineEvenIfLate() {
        Loan loan = new Loan("User", "Test Book", "BOOK",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(2).toString(),
                true, 0);
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testPayFineResetsToZero() {
        Loan loan = new Loan("User", "Test Book", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 10);
        loan.payFine();
        assertEquals(0, loan.getFine());
    }

    @Test
    void testMarkReturned() {
        Loan loan = new Loan("User", "Test Book", "BOOK");
        loan.markReturned();
        assertTrue(loan.isReturned());
    }

    @Test
    void testToStringFormat() {
        Loan loan = new Loan("User", "Test Book", "BOOK");
        String s = loan.toString();
        assertTrue(s.contains("BOOK"));
        assertTrue(s.contains("Test Book"));
        assertTrue(s.contains("User"));
        assertTrue(s.contains("Returned"));
    }

    @Test
    void testMediaTypeCaseInsensitive() {
        Loan loan = new Loan("User", "Test Book", "Cd");
        assertEquals("CD", loan.getMediaType());
    }
}
