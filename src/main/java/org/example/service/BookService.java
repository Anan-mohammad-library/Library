package org.example.service;

import org.example.domain.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {

    private static final String FILE_PATH = "books.txt";
    private List<Book> books;

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

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
}
