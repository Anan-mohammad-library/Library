package org.example;

import org.example.domain.Book;
import org.example.strategy.AuthorSearchStrategy;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorSearchStrategyTest {

    @Test
    void testNullBooks() {
        AuthorSearchStrategy s = new AuthorSearchStrategy();
        assertTrue(s.search(null, "abc").isEmpty());
    }

    @Test
    void testEmptyBooks() {
        AuthorSearchStrategy s = new AuthorSearchStrategy();
        assertTrue(s.search(List.of(), "abc").isEmpty());
    }

    @Test
    void testNullKeyword() {
        AuthorSearchStrategy s = new AuthorSearchStrategy();
        assertTrue(s.search(List.of(new Book("Clean", "Bob", "1")), null).isEmpty());
    }

    @Test
    void testMatchFound() {
        AuthorSearchStrategy s = new AuthorSearchStrategy();
        List<Book> books = List.of(new Book("Clean", "Robert", "1"));
        assertEquals(1, s.search(books, "ro").size());
    }

    @Test
    void testMatchNotFound() {
        AuthorSearchStrategy s = new AuthorSearchStrategy();
        List<Book> books = List.of(new Book("Clean", "Robert", "1"));
        assertEquals(0, s.search(books, "xyz").size());
    }
}
