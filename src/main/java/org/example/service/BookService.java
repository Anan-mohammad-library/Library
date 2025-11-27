package org.example.service;

import org.example.domain.Book;
import org.example.strategy.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private static final String FILE_PATH = "books.txt";
    private List<Book> books;
    private BookSortStrategy sortStrategy;
    private BookSearchStrategy searchStrategy;

    public BookService() {
        books = loadBooks();
        if (books.isEmpty()) {
            books.add(new Book("Java Basics", "John Doe", "12345"));
            books.add(new Book("Clean Code", "Robert C. Martin", "999"));
            books.add(new Book("Effective Java", "Joshua Bloch", "111"));
            saveBooks();
        }
    }

    public void addBook(String title, String author, String isbn) {
        books.add(new Book(title, author, isbn));
        saveBooks();
    }

    // --- Strategy Pattern for Sorting ---
    public void setSortStrategy(BookSortStrategy strategy) {
        this.sortStrategy = strategy;
    }

    public BookSortStrategy getSortStrategy() {
        return sortStrategy;
    }

    public List<Book> sort() {
        if (sortStrategy == null) return books;
        return sortStrategy.sort(books);
    }

    // --- Strategy Pattern for Searching ---
    public void setSearchStrategy(BookSearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    public BookSearchStrategy getSearchStrategy() {
        return searchStrategy;
    }

    public List<Book> search(String keyword) {
        if (searchStrategy == null) return books;
        return searchStrategy.search(books, keyword);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    private void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book b : books) {
                writer.write(b.getTitle() + "|" + b.getAuthor() + "|" + b.getIsbn());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Book> loadBooks() {
        List<Book> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    list.add(new Book(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
