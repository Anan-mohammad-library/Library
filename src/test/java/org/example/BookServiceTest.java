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
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();
        service = new BookService();
    }

    @Test
    void testAddBookAndSearch() {
        service.addBook("Java Basics", "John Doe", "12345");
        List<Book> result = service.search("Java Basics");
        assertFalse(result.isEmpty());
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
        assertTrue(result.isEmpty());
    }

    @Test
    void testCaseInsensitiveSearch() {
        service.addBook("Spring Framework", "Rod Johnson", "555");
        List<Book> result = service.search("spring");
        assertEquals(1, result.size());
    }

    @Test
    void testMultipleBooksSearch() {
        service.addBook("Java 101", "Alice", "101");
        service.addBook("Java Advanced", "Bob", "102");
        List<Book> result = service.search("Java");
        assertEquals(2, result.size());
    }

    @Test
    void testBooksPersistedToFile() {
        service.addBook("Persistence Test", "Author X", "999");
        BookService newService = new BookService();
        List<Book> result = newService.search("Persistence Test");
        assertEquals(1, result.size());
    }

    @Test
    void testAddDuplicateIsbn() {
        service.addBook("Book A", "Author A", "123");
        service.addBook("Book B", "Author B", "123");
        List<Book> result = service.search("123");
        assertEquals(2, result.size());
    }

    @Test
    void testAddMultipleBooksAndSearch() {
        service.addBook("Book 1", "Author 1", "001");
        service.addBook("Book 2", "Author 2", "002");
        service.addBook("Book 3", "Author 3", "003");
        List<Book> result = service.search("Book");
        assertEquals(3, result.size());
    }

    @Test
    void testSearchPartialTitle() {
        service.addBook("Java Concurrency", "Author X", "333");
        List<Book> result = service.search("Concur");
        assertEquals(1, result.size());
        assertEquals("Java Concurrency", result.get(0).getTitle());
    }

    @Test
    void testSearchPartialAuthor() {
        service.addBook("Book X", "James Gosling", "444");
        List<Book> result = service.search("Gosling");
        assertEquals(1, result.size());
        assertEquals("Book X", result.get(0).getTitle());
    }

    @Test
    void testSearchMultipleMatches() {
        service.addBook("Java Basics", "John Doe", "123");
        service.addBook("Advanced Java", "John Doe", "124");
        List<Book> result = service.search("John Doe");
        assertEquals(2, result.size());
    }

    @Test
    void testEmptySearchReturnsAllBooks() {
        service.addBook("Book A", "Author A", "001");
        service.addBook("Book B", "Author B", "002");
        List<Book> result = service.search("");
        assertEquals(2, result.size());
    }
}
