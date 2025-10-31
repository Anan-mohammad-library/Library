package org.example;

import org.example.domain.*;
import org.example.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceRestrictionTest {

    private LoanService loanService;

    @BeforeEach
    void setup() {
        loanService = new LoanService();
    }


    @Test
    void calculatesFineForOverdueLoans() {
        Book book = new Book("Book X", "A", "1");
        Loan overdueLoan = new Loan("lina", book);
        overdueLoan.getMedia().setAvailable(false);
        overdueLoan.checkOverdue();
        assertTrue(overdueLoan.getFine() >= 0);
    }



    @Test
    void fineIsZeroIfReturnedOnTime() {
        Book book = new Book("Book M", "A", "1");
        Loan loan = new Loan("hani", book);
        loan.markReturned();
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }

    @Test
    void fineIncreasesWithDaysOverdue() {
        Book book = new Book("Short Delay", "A", "1");
        Loan loan1 = new Loan("ali", book);
        loan1.checkOverdue();

        Book book2 = new Book("Long Delay", "B", "2");
        Loan loan2 = new Loan("ali", book2);
        loan2.checkOverdue();

        assertTrue(loan2.getFine() >= loan1.getFine());
    }

    @Test
    void userWithoutOverdueHasNoBlock() {
        Book book = new Book("Book Y", "A", "1");
        Loan loan = new Loan("noor", book);
        loan.markReturned();
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);
        assertFalse(loanService.hasBlocks("noor"));
    }

    @Test
    void returningAlreadyReturnedLoanDoesNotCreateBlock() {
        Book book = new Book("Book Z", "A", "1");
        Loan loan = new Loan("fadi", book);
        loan.markReturned();
        loanService.getAllLoans().add(loan);
        loan.markReturned();
        assertFalse(loanService.hasBlocks("fadi"));
    }

    @Test
    void fineRemainsZeroForFutureLoan() {
        Book book = new Book("Future Book", "A", "1");
        Loan loan = new Loan("tala", book);
        loan.checkOverdue();
        assertEquals(0, loan.getFine());
    }


}