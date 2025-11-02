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

        assertEquals(2, b.getFine());   // 1 NIS/day
        assertEquals(3*20, c.getFine()); // 20 NIS/day

        assertTrue(c.getFine() > b.getFine());
    }
}
