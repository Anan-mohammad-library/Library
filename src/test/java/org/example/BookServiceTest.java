package org.example;

import org.example.domain.Book;
import org.example.service.BookService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Simple unit tests for BookService class.
 */
public class BookServiceTest {

    @Test
    void testAddBook() {
        BookService service = new BookService();
        service.addBook("Java Basics", "John Doe", "12345");

        List<Book> result = service.search("Java Basics");
        assertFalse(result.isEmpty(), "Book should be found after adding");
        assertEquals("Java Basics", result.get(0).getTitle());
    }

    @Test
    void testSearchNotFound() {
        BookService service = new BookService();
        List<Book> result = service.search("Unknown Title");
        assertTrue(result.isEmpty(), "No books should match this keyword");
    }
}
