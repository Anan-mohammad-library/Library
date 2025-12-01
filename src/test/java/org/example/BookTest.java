package org.example;

import org.example.domain.Book;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private static final String FILE_PATH = "books.txt";

    @BeforeEach
    void cleanFile() {
        File f = new File(FILE_PATH);
        if (f.exists()) f.delete();
    }

    @Test
    void testConstructorAndGetters() {
        Book b = new Book("Clean Code", "Robert Martin", "111");
        assertEquals("Clean Code", b.getTitle());
        assertEquals("Robert Martin", b.getAuthor());
        assertEquals("111", b.getIsbn());
    }

    @Test
    void testLoanDays() {
        Book b = new Book("Clean Code", "Robert Martin", "111");
        assertEquals(28, b.getLoanDays());
    }

    @Test
    void testCalculateFine() {
        Book b = new Book("Clean Code", "Robert Martin", "111");
        assertEquals(100, b.calculateFine(10));
    }

    @Test
    void testToString() {
        Book b = new Book("Clean Code", "Robert Martin", "111");
        String s = b.toString();
        assertTrue(s.contains("Clean Code"));
        assertTrue(s.contains("Robert Martin"));
        assertTrue(s.contains("111"));
    }

    @Test
    void testSaveAndLoad() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Book1", "Auth1", "111"));
        list.add(new Book("Book2", "Auth2", "222"));

        Book.saveBooksToFile(list);

        List<Book> loaded = Book.loadBooksFromFile();
        assertEquals(2, loaded.size());
        assertEquals("Book1", loaded.get(0).getTitle());
        assertEquals("Auth2", loaded.get(1).getAuthor());
    }

    @Test
    void testLoadWhenFileMissing() {
        List<Book> loaded = Book.loadBooksFromFile();
        assertTrue(loaded.isEmpty());
    }

    @Test
    void testLoadWithInvalidLines() throws Exception {
        try (FileWriter w = new FileWriter(FILE_PATH)) {
            w.write("invalid\n");
            w.write("BookX|AuthorX\n");
            w.write("BookY|AuthorY|333\n");
        }

        List<Book> loaded = Book.loadBooksFromFile();
        assertEquals(1, loaded.size());
        assertEquals("BookY", loaded.get(0).getTitle());
    }
}
