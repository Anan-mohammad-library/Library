package org.example.strategy;

import org.example.domain.Book;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortByTitleStrategy implements BookSortStrategy {

    @Override
    public List<Book> sort(List<Book> books) {

        if (books == null || books.isEmpty())
            return List.of();

        return books.stream()
                .sorted(Comparator.comparing(
                        b -> b.getTitle() == null ? "" : b.getTitle()))
                .collect(Collectors.toList());
    }
}
