package org.example;

import org.example.domain.CD;
import org.example.service.CDService;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CDServiceTest {

    private CDService service;
    private static final String FILE = "cds.txt";

    @BeforeEach
    void setup() {
        File file = new File(FILE);
        if (file.exists()) file.delete();
        service = new CDService();
    }

    @Test
    void testAddCD() {
        service.addCD("Thriller", "Michael Jackson", "1");
        List<CD> res = service.search("Thriller");

        assertEquals(1, res.size());
        assertEquals("Thriller", res.get(0).getTitle());
        assertEquals("Michael Jackson", res.get(0).getArtist());
    }

    @Test
    void testSearchCDByArtist() {
        service.addCD("Dangerous", "Michael Jackson", "2");
        List<CD> res = service.search("Michael");

        assertEquals(1, res.size());
        assertEquals("Dangerous", res.get(0).getTitle());
    }

    @Test
    void testNoCDFound() {
        List<CD> res = service.search("Unknown");
        assertTrue(res.isEmpty());
    }
}
