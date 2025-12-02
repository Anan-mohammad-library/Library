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
    }

    @Test
    void testBorrowCD() {
        loanService.borrowCD("ali", "CD Music");
        assertEquals(1, loanService.getAllLoans().size());
    }

    @Test
    void testBorrowBookBlocked() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 5);

        loanService.getAllLoans().add(loan);
        assertTrue(loanService.hasBlocks("ali"));
        loanService.borrowBook("ali", "Another Book");
        assertEquals(1, loanService.getAllLoans().size());
    }



    @Test
    void testHasBlocksReturnedWithFine() {
        Loan loan = new Loan("moh", "Book A", "BOOK");
        loan.payFine();     // fine = 0
        loan.markReturned(); // returned = true
        loanService.getAllLoans().add(loan);
        assertFalse(loanService.hasBlocks("moh"));
    }

    @Test
    void testHasBlocksReturnedNoFine() {
        Loan loan = new Loan("moh", "Book A", "BOOK",
                LocalDate.now().toString(),
                LocalDate.now().plusDays(5).toString(),
                true, 0);
        loanService.getAllLoans().add(loan);
        assertFalse(loanService.hasBlocks("moh"));
    }

    @Test
    void testHasBlocksFineButReturnedTrue() {
        Loan loan = new Loan(
                "moh", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                true,
                10
        );
        loanService.getAllLoans().add(loan);

        assertFalse(loanService.hasBlocks("moh"));
    }




    @Test
    void testHasBlocksNoLoansForUser() {
        assertFalse(loanService.hasBlocks("nobody"));
    }



    @Test
    void testShowAllLoansEmpty() {
        loanService.showAllLoans();
        assertTrue(loanService.getAllLoans().isEmpty());
    }

    @Test
    void testShowAllLoansNonEmpty() {
        loanService.borrowBook("ali", "Book A");
        loanService.showAllLoans();
        assertEquals(1, loanService.getAllLoans().size());
    }




    @Test
    void testShowUserLoansEmpty() {
        loanService.showUserLoans("ali");
        assertTrue(loanService.getAllLoans().isEmpty());
    }

    @Test
    void testShowUserLoansExists() {
        loanService.borrowBook("ali", "Book A");
        loanService.showUserLoans("ali");
        assertEquals(1, loanService.getAllLoans().size());
    }




    @Test
    void testShowAllOverduesNoOverdue() {
        loanService.borrowBook("ali", "Book A");
        loanService.showAllOverdues();
        assertEquals(1, loanService.getAllLoans().size());
    }

    @Test
    void testShowAllOverduesExists() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(30).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);
        loanService.showAllOverdues();
        assertTrue(loan.getFine() > 0);
    }



    @Test
    void testShowUserOverduesNone() {
        loanService.showUserOverdues("ali");
    }

    @Test
    void testShowUserOverduesExists() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(20).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);
        loanService.showUserOverdues("ali");
        assertTrue(loan.getFine() > 0);
    }



    @Test
    void testPayFineNone() {
        loanService.payFine("ali");
    }

    @Test
    void testPayFineExists() {
        Loan loan = new Loan("ali", "Book A", "BOOK",
                LocalDate.now().minusDays(15).toString(),
                LocalDate.now().minusDays(5).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);
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
        Loan loan = new Loan("user1", "Book A", "BOOK",
                LocalDate.now().minusDays(25).toString(),
                LocalDate.now().minusDays(10).toString(),
                false, 0);
        loan.checkOverdue();
        loanService.getAllLoans().add(loan);
        List<String> overdueUsers = loanService.getAllUsersWithOverdues();
        assertTrue(overdueUsers.contains("user1"));
    }
}
