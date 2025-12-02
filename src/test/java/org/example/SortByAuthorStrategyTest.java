package org.example;

import org.example.domain.Book;
import org.example.strategy.SortByAuthorStrategy;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortByAuthorStrategyTest {

    @Test
    void testNullBooks() {
        SortByAuthorStrategy s = new SortByAuthorStrategy();
        assertTrue(s.sort(null).isEmpty());
    }

    @Test
    void testEmptyBooks() {
        SortByAuthorStrategy s = new SortByAuthorStrategy();
        assertTrue(s.sort(List.of()).isEmpty());
    }

    @Test
    void testSortNormal() {
        SortByAuthorStrategy s = new SortByAuthorStrategy();
        List<Book> books = List.of(
                new Book("B1", "Charlie", "1"),
                new Book("B2", "Alice", "2"),
                new Book("B3", "Bob", "3")
        );
        List<Book> sorted = s.sort(books);
        assertEquals("Alice", sorted.get(0).getAuthor());
        assertEquals("Bob", sorted.get(1).getAuthor());
        assertEquals("Charlie", sorted.get(2).getAuthor());
    }

    @Test
    void testNullAuthorsInsideBooks() {
        SortByAuthorStrategy s = new SortByAuthorStrategy();
        List<Book> books = List.of(
                new Book("B1", null, "1"),
                new Book("B2", "Alice", "2")
        );
        List<Book> sorted = s.sort(books);
        assertEquals("Alice", sorted.get(1).getAuthor());
    }
}
