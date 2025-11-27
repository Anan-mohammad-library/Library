package org.example.strategy;

import org.example.domain.Book;
import java.util.List;

public interface BookSortStrategy {
    List<Book> sort(List<Book> books);
}
