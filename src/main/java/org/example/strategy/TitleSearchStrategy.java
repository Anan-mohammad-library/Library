package org.example.strategy;

import org.example.domain.Book;
import java.util.List;
import java.util.stream.Collectors;

public class TitleSearchStrategy implements BookSearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String keyword) {

        if (books == null || books.isEmpty())
            return List.of();

        if (keyword == null || keyword.isBlank())
            return List.of();

        return books.stream()
                .filter(b -> b.getTitle() != null &&
                        b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
