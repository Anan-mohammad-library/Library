package org.example.service;
import org.example.domain.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class BookService {


    private final List<Book> books = new ArrayList<>();

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
    }

    public List<Book> search(String keyword) {
        String lower = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower)
                        || b.getAuthor().toLowerCase().contains(lower)
                        || b.getIsbn().contains(keyword))
                .collect(Collectors.toList());
    }
    public List<Book> getBooks() {
        return books;
    }
}
