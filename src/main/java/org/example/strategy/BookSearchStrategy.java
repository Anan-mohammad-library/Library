package org.example.strategy;

import org.example.domain.Book;
import java.util.List;

public interface BookSearchStrategy {
    List<Book> search(List<Book> books, String keyword);
}
