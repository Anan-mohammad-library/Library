package org.example;


import org.example.domain.Book;
import org.example.strategy.SortByAuthorStrategy;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortByAuthorStrategyTest {

    @Test
    void testAuthorSorting() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Clean Code", "Robert Martin", "111"));
        list.add(new Book("Effective Java", "Joshua Bloch", "222"));
        list.add(new Book("Design Patterns", "Erich Gamma", "333"));

        SortByAuthorStrategy strategy = new SortByAuthorStrategy();
        List<Book> sorted = strategy.sort(list);

        assertEquals("Erich Gamma", sorted.get(0).getAuthor());
        assertEquals("Joshua Bloch", sorted.get(1).getAuthor());
        assertEquals("Robert Martin", sorted.get(2).getAuthor());
    }


    @Test
    void testSortingWithSameAuthor() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Book A", "Same Author", "111"));
        list.add(new Book("Book B", "Same Author", "222"));

        SortByAuthorStrategy strategy = new SortByAuthorStrategy();
        List<Book> sorted = strategy.sort(list);


        assertEquals("Book A", sorted.get(0).getTitle());
        assertEquals("Book B", sorted.get(1).getTitle());
    }

    @Test
    void testEmptyList() {
        List<Book> list = new ArrayList<>();
        SortByAuthorStrategy strategy = new SortByAuthorStrategy();
        List<Book> sorted = strategy.sort(list);

        assertTrue(sorted.isEmpty());
    }
}
