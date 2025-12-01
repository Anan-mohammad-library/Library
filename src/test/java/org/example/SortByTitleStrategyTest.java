package org.example;

import org.example.domain.Book;
import org.example.strategy.SortByTitleStrategy;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortByTitleStrategyTest {

    @Test
    void testSort() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("C", "auth1", "1"));
        list.add(new Book("A", "auth2", "2"));
        list.add(new Book("B", "auth3", "3"));

        list = new SortByTitleStrategy().sort(list);

        assertEquals("A", list.get(0).getTitle());
        assertEquals("B", list.get(1).getTitle());
        assertEquals("C", list.get(2).getTitle());
    }
}
