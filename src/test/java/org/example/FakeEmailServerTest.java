package org.example;

import org.example.service.FakeEmailServer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FakeEmailServerTest {

    @Test
    void testSendEmailDoesNotThrow() {
        FakeEmailServer server = new FakeEmailServer();
        assertDoesNotThrow(() -> server.sendEmail("test@mail.com", "Hello"));
    }
    @Test
    void testWriteEmail() {
        FakeEmailServer server = new FakeEmailServer();
        assertDoesNotThrow(() -> server.sendEmail("Ali", "test msg"));
    }

}
