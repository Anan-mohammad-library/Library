package org.example.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book entity that can be loaded from a text file (books.txt).
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "books.txt";

    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }

    // =====================================================
    // 📚 تحميل الكتب فقط من الملف (لا إنشاء كتب جديدة)
    // =====================================================
    public static List<Book> loadBooksFromFile() {
        List<Book> books = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("⚠️ No books.txt file found. Please add it first.");
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    books.add(new Book(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading books.txt file: " + e.getMessage());
        }

        return books;
    }

    // =====================================================
    // 🔍 البحث عن كتاب داخل الملف بدون إنشاء جديد
    // =====================================================
    public static Book findBookByTitle(String title) {
        return loadBooksFromFile().stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}
