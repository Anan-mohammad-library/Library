package org.example;

import org.example.domain.Loan;
import org.example.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceRestrictionTest {

    private LoanService loanService;

    @BeforeEach
    void setup() {
        loanService = new LoanService();
    }

    @Test
    void blocksBorrowWhenUserHasOverdue() {
        Loan l = new Loan(
                "sam",
                "Some Book",
                "BOOK",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(),
                false,
                0
        );
        l.checkOverdue();
        loanService.getAllLoans().add(l);

        loanService.borrowBook("sam", "Another Book");

        assertTrue(loanService.hasBlocks("sam"));
    }




    @Test
    void calculatesCDFineCorrectly() {
        Loan l = new Loan(
                "moh",
                "Thriller",
                "CD",
                LocalDate.now().minusDays(10).toString(),
                LocalDate.now().minusDays(3).toString(),
                false,
                0
        );
        l.checkOverdue();
        assertEquals(3 * 20, l.getFine());
    }


    @Test
    void blocksUserWithOverdueAfterBorrowAttempt() {
        Loan l = new Loan(
                "tom",
                "Book A",
                "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false,
                0
        );
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
}
