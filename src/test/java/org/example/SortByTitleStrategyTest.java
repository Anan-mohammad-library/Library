package org.example;

import org.example.domain.Book;
import org.example.strategy.SortByTitleStrategy;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortByTitleStrategyTest {

    @Test
    void testNullBooks() {
        SortByTitleStrategy s = new SortByTitleStrategy();
        assertTrue(s.sort(null).isEmpty());
    }

    @Test
    void testEmptyBooks() {
        SortByTitleStrategy s = new SortByTitleStrategy();
        assertTrue(s.sort(List.of()).isEmpty());
    }

    @Test
    void testSortNormal() {
        SortByTitleStrategy s = new SortByTitleStrategy();
        List<Book> books = List.of(
                new Book("Charlie", "X", "1"),
                new Book("Alice", "Y", "2"),
                new Book("Bob", "Z", "3")
        );
        List<Book> sorted = s.sort(books);
        assertEquals("Alice", sorted.get(0).getTitle());
        assertEquals("Bob", sorted.get(1).getTitle());
        assertEquals("Charlie", sorted.get(2).getTitle());
    }

    @Test
    void testNullTitlesInsideBooks() {
        SortByTitleStrategy s = new SortByTitleStrategy();
        List<Book> books = List.of(
                new Book(null, "X", "1"),
                new Book("Alice", "Y", "2")
        );
        List<Book> sorted = s.sort(books);
        assertEquals("Alice", sorted.get(1).getTitle());
    }
}
