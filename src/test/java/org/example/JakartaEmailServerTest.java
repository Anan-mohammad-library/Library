package org.example;

import org.example.service.JakartaEmailServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JakartaEmailServerTest {

    @Test
    void testSendEmailIgnoreExceptions() {
        JakartaEmailServer server = new JakartaEmailServer();

        assertDoesNotThrow(() -> server.sendEmail("test@test.com", "Hello"));
    }
}
