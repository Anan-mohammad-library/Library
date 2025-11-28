package org.example;

import org.example.domain.Book;
import org.example.service.BookService;
import org.example.strategy.*;
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
        service.setSearchStrategy(new TitleSearchStrategy());
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
    void testSearchNotFound() {
        List<Book> result = service.search("Unknown Title");
        assertTrue(result.isEmpty());
    }

    @Test
    void testCaseInsensitiveSearch() {
        service.setSearchStrategy(new TitleSearchStrategy());
        service.addBook("Spring Framework", "Rod Johnson", "555");
        List<Book> result = service.search("spring");
        assertEquals(1, result.size());
    }

    @Test
    void testBooksPersistedToFile() {
        service.addBook("Persistence Test", "Author X", "999");
        BookService newService = new BookService();
        newService.setSearchStrategy(new TitleSearchStrategy());
        List<Book> result = newService.search("Persistence Test");
        assertEquals(1, result.size());
    }

    @Test
    void testAddMultipleBooksAndSearch() {
        service.addBook("Book 1", "Author 1", "001");
        service.addBook("Book 2", "Author 2", "002");
        service.addBook("Book 3", "Author 3", "003");
        service.setSearchStrategy(new TitleSearchStrategy());
        List<Book> result = service.search("Book");
        assertEquals(3, result.size());
    }

    @Test
    void testSearchPartialTitle() {
        service.setSearchStrategy(new TitleSearchStrategy());
        service.addBook("Java Concurrency", "Author X", "333");
        List<Book> result = service.search("Concur");
        assertEquals(1, result.size());
        assertEquals("Java Concurrency", result.get(0).getTitle());
    }

    @Test
    void testSearchPartialAuthor() {
        service.setSearchStrategy(new AuthorSearchStrategy());
        service.addBook("Book X", "James Gosling", "444");
        List<Book> result = service.search("Gosling");
        assertEquals(1, result.size());
        assertEquals("Book X", result.get(0).getTitle());
    }



}
