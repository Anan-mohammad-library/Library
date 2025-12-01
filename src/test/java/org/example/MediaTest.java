package org.example;

import org.example.domain.Media;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// subclass وهمي لاختبار abstract class
class TestMedia extends Media {
    public TestMedia(String title) {
        super(title);
    }

    @Override
    public int getLoanDays() {
        return 10;
    }

    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 2;
    }
}

public class MediaTest {

    @Test
    void testGetTitle() {
        Media m = new TestMedia("Test Title");
        assertEquals("Test Title", m.getTitle());
    }



    @Test
    void testLoanDaysImplementation() {
        Media m = new TestMedia("Test");
        assertEquals(10, m.getLoanDays());
    }

    @Test
    void testCalculateFineImplementation() {
        Media m = new TestMedia("Test");
        assertEquals(20, m.calculateFine(10));
    }

    @Test
    void testToStringAvailable() {
        Media m = new TestMedia("Book");
        assertEquals("Book (Available)", m.toString());
    }

    @Test
    void testToStringBorrowed() {
        Media m = new TestMedia("Book");
        m.setAvailable(false);
        assertEquals("Book (Borrowed)", m.toString());
    }
}
