package org.example.strategy;

import org.example.domain.Book;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorSearchStrategy implements BookSearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
