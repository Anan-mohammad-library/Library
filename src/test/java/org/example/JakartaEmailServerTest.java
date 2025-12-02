package org.example;

import org.example.service.JakartaEmailServer;
import org.junit.jupiter.api.Test;

public class JakartaEmailServerTest {

    @Test
    void testSendEmailIgnoreExceptions() {
        JakartaEmailServer server = new JakartaEmailServer();
        try {
            server.sendEmail("test@test.com", "Hello");
        } catch (Exception ignored) {

        }
    }
}
