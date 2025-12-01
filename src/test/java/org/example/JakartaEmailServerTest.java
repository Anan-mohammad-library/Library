package org.example;

import org.example.service.JakartaEmailServer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JakartaEmailServerTest {

    @Test
    void testThrowsIfEnvMissing() {
        JakartaEmailServer server = new JakartaEmailServer();
        assertThrows(RuntimeException.class, () -> server.sendEmail("a@mail.com", "msg"));
    }
    @Test
    void testThrowsWhenEnvMissing() {
        JakartaEmailServer server = new JakartaEmailServer();
        assertThrows(RuntimeException.class,
                () -> server.sendEmail("A@mail.com", "msg"));
    }

}
