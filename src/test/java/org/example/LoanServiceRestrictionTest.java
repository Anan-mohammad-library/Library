package org.example;

import org.example.domain.Loan;
import org.example.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceRestrictionTest {

    @Test
    void blocksBorrowWhenUserHasOverdue() {
        LoanService ls = new LoanService();


        Loan l = new Loan("sam", "Some Book",
                LocalDate.now().minusDays(40).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);
        l.checkOverdue();

        ls.getAllLoans().add(l);


        ls.borrowBook("sam", "Another Book");


        assertTrue(ls.hasBlocks("sam"));
    }
}
