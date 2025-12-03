package org.example;
import org.example.domain.Book;
import org.example.strategy.TitleSearchStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TitleSearchStrategyTest {

    private final TitleSearchStrategy strategy = new TitleSearchStrategy();

    @Test
    void testNullBookListReturnsEmpty() {
        assertTrue(strategy.search(null, "java").isEmpty());
    }

    @Test
    void testEmptyBookListReturnsEmpty() {
        assertTrue(strategy.search(List.of(), "java").isEmpty());
    }

    @Test
    void testNullKeywordReturnsEmpty() {
        List<Book> books = List.of(new Book("Java", "Author", "123"));
        assertTrue(strategy.search(books, null).isEmpty());
    }

    @Test
    void testBlankKeywordReturnsEmpty() {
        List<Book> books = List.of(new Book("Java", "Author", "123"));
        assertTrue(strategy.search(books, "   ").isEmpty());
    }

    @Test
    void testBookWithNullTitleIsIgnored() {
        Book b = new Book(null, "Author", "123");
        List<Book> result = strategy.search(List.of(b), "java");
        assertTrue(result.isEmpty());
    }

    @Test
    void testMatchingTitleIsReturned() {
        Book b = new Book("Java Programming", "Author", "123");
        List<Book> result = strategy.search(List.of(b), "java");

        assertEquals(1, result.size());
        assertEquals(b, result.get(0));
    }

    @Test
    void testNonMatchingTitleIsNotReturned() {
        Book b = new Book("Python Guide", "Author", "123");
        List<Book> result = strategy.search(List.of(b), "java");

        assertTrue(result.isEmpty());
    }
}
