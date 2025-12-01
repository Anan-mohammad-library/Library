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
        service = new BookService(); // will auto-load defaults if no file exists
    }

    @Test
    void testConstructorLoadsDefaultBooks() {
        List<Book> result = service.getAllBooks();
        assertEquals(3, result.size()); // 3 default books
    }

    @Test
    void testAddBook() {
        service.addBook("New Book", "Author X", "777");
        List<Book> result = service.getAllBooks();
        assertEquals(4, result.size()); // 3 default +1
    }

    @Test
    void testSaveAndLoadBooks() {
        service.addBook("Persistence Test", "My Author", "999");
        BookService newService = new BookService();
        List<Book> result = newService.search("Persistence Test");
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Persistence Test")));
    }


    @Test
    void testSortByTitle() {
        service.setSortStrategy(new SortByTitleStrategy());
        List<Book> result = service.sort();
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Effective Java", result.get(1).getTitle());
        assertEquals("Java Basics", result.get(2).getTitle());
    }

    @Test
    void testSortByAuthor() {
        service.setSortStrategy(new SortByAuthorStrategy());
        List<Book> result = service.sort();
        assertEquals("John Doe", result.get(0).getAuthor());
        assertEquals("Joshua Bloch", result.get(1).getAuthor());
        assertEquals("Robert C. Martin", result.get(2).getAuthor());
    }

    @Test
    void testSortWithNullStrategyReturnsOriginal() {
        service.setSortStrategy(null);
        List<Book> result = service.sort();
        assertEquals(service.getAllBooks().size(), result.size());
    }

    @Test
    void testGetSortStrategy() {
        SortByTitleStrategy s = new SortByTitleStrategy();
        service.setSortStrategy(s);
        assertEquals(s, service.getSortStrategy());
    }

    @Test
    void testGetSearchStrategy() {
        TitleSearchStrategy s = new TitleSearchStrategy();
        service.setSearchStrategy(s);
        assertEquals(s, service.getSearchStrategy());
    }

    @Test
    void testSearchWithNullStrategyReturnsOriginal() {
        service.setSearchStrategy(null);
        List<Book> result = service.search("anything");
        assertEquals(service.getAllBooks().size(), result.size());
    }

    @Test
    void testSearchByTitle() {
        service.setSearchStrategy(new TitleSearchStrategy());
        List<Book> result = service.search("Java");
        assertEquals(2, result.size());
    }

    @Test
    void testSearchByAuthor() {
        service.setSearchStrategy(new AuthorSearchStrategy());
        List<Book> result = service.search("Martin");
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    void testSearchBookNotFound() {
        service.setSearchStrategy(new TitleSearchStrategy());
        List<Book> result = service.search("UnknownTitleXYZ");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllBooksReturnsCopyNotReference() {
        List<Book> books = service.getAllBooks();
        books.clear();
        assertEquals(3, service.getAllBooks().size()); // internal list not affected
    }

}
