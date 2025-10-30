package org.example;


import org.example.domain.Book;
import org.example.service.BookService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService service;
    private static final String FILE_PATH = "books.txt";

    @BeforeEach
    void setup() {
        // Delete the file before each test to start fresh
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();

        service = new BookService();
    }

    @Test
    void testAddBook() {
        service.addBook("Java Basics", "John Doe", "12345");
        List<Book> result = service.search("Java Basics");

        assertFalse(result.isEmpty(), "Book should be found after adding");
        assertEquals("Java Basics", result.get(0).getTitle());
        assertEquals("John Doe", result.get(0).getAuthor());
        assertEquals("12345", result.get(0).getIsbn());
    }

    @Test
    void testSearchByTitle() {
        service.addBook("Clean Code", "Robert C. Martin", "999");
        List<Book> result = service.search("Clean");
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void testSearchByAuthor() {
        service.addBook("Effective Java", "Joshua Bloch", "111");
        List<Book> result = service.search("Joshua");
        assertEquals(1, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
    }

    @Test
    void testSearchByIsbn() {
        service.addBook("Java Patterns", "Gamma", "222");
        List<Book> result = service.search("222");
        assertEquals(1, result.size());
        assertEquals("Java Patterns", result.get(0).getTitle());
    }

    @Test
    void testSearchNotFound() {
        List<Book> result = service.search("Unknown Title");
        assertTrue(result.isEmpty(), "No books should match this keyword");
    }

    @Test
    void testCaseInsensitiveSearch() {
        service.addBook("Spring Framework", "Rod Johnson", "555");
        List<Book> result = service.search("spring");
        assertEquals(1, result.size(), "Search should be case-insensitive");
    }

    @Test
    void testMultipleBooksSearch() {
        service.addBook("Java 101", "Alice", "101");
        service.addBook("Java Advanced", "Bob", "102");

        List<Book> result = service.search("Java");
        assertEquals(2, result.size(), "Should return both books matching keyword 'Java'");
    }

    @Test
    void testBooksPersistedToFile() {
        service.addBook("Persistence Test", "Author X", "999");
        BookService newService = new BookService();
        List<Book> result = newService.search("Persistence Test");
        assertEquals(1, result.size(), "Book should be loaded from file in new service instance");
    }

    @Test
    void testAddDuplicateIsbn() {
        service.addBook("Book A", "Author A", "123");
        service.addBook("Book B", "Author B", "123"); // duplicate ISBN

        // Should allow adding duplicates with current implementation, but you could prevent it:
        List<Book> result = service.search("123");
        assertEquals(2, result.size(), "Duplicate ISBNs currently allowed, returns both");
    }
}


