package org.example.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Book extends Media implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "books.txt";
    private static final int LOAN_DAYS = 28;

    public static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Book.class.getName());


    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        super(title);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public int getLoanDays() { return LOAN_DAYS; }

    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 10.0;
    }

    @Override
    public String toString() {
        return "Book: " + title + " by " + author + " (ISBN: " + isbn + ")";
    }

    public static List<Book> loadBooksFromFile() {
        List<Book> books = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return books;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    books.add(new Book(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            logger.severe("Error reading books.txt: " + e.getMessage());
        }
        return books;
    }

    public static void saveBooksToFile(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book b : books) {
                writer.write(b.getTitle() + "|" + b.getAuthor() + "|" + b.getIsbn());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Error writing to books.txt: " + e.getMessage());
        }
    }
}
