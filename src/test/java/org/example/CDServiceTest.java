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
        assertEquals("1", res.get(0).getId());
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

    @Test
    void testCaseInsensitiveSearch() {
        service.addCD("Bad", "Michael Jackson", "3");
        List<CD> res = service.search("bad");
        assertEquals(1, res.size());
    }

    @Test
    void testSearchById() {
        service.addCD("HIStory", "Michael Jackson", "4");
        List<CD> res = service.search("4");
        assertEquals(1, res.size());
        assertEquals("HIStory", res.get(0).getTitle());
    }

    @Test
    void testMultipleCDsSearch() {
        service.addCD("Album1", "Artist1", "10");
        service.addCD("Album2", "Artist2", "11");
        service.addCD("Album3", "Artist3", "12");
        List<CD> res = service.search("Album");
        assertEquals(3, res.size());
    }

    @Test
    void testCDsPersistedToFile() {
        service.addCD("FileTest", "ArtistX", "99");
        CDService newService = new CDService();
        List<CD> res = newService.search("FileTest");
        assertEquals(1, res.size());
    }

    @Test
    void testAddDuplicateId() {
        service.addCD("CD A", "Artist A", "100");
        service.addCD("CD B", "Artist B", "100");
        List<CD> res = service.search("100");
        assertEquals(2, res.size());
    }

    @Test
    void testSearchPartialTitle() {
        service.addCD("Greatest Hits", "Queen", "200");
        List<CD> res = service.search("Greatest");
        assertEquals(1, res.size());
    }

    @Test
    void testSearchPartialArtist() {
        service.addCD("Album X", "Freddie Mercury", "201");
        List<CD> res = service.search("Mercury");
        assertEquals(1, res.size());
    }

    @Test
    void testEmptySearchReturnsAllCDs() {
        service.addCD("CD1", "Artist1", "300");
        service.addCD("CD2", "Artist2", "301");
        List<CD> res = service.search("");
        assertEquals(2, res.size());
    }
}
