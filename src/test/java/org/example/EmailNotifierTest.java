package org.example;

import org.example.service.EmailNotifier;
import org.example.service.EmailServer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailNotifierTest {


    static class FakeEmailServer implements EmailServer {
        String lastTo;
        String lastMsg;
        boolean sendCalled = false;

        @Override
        public void sendEmail(String to, String message) {
            lastTo = to;
            lastMsg = message;
            sendCalled = true;
        }
    }

    @Test
    void testNotifyCallsSendEmail() {
        FakeEmailServer fake = new FakeEmailServer();
        EmailNotifier notifier = new EmailNotifier(fake);

        notifier.notify("user@test.com", "Hello!");

        assertTrue(fake.sendCalled, "sendEmail should have been called");
        assertEquals("user@test.com", fake.lastTo);
        assertEquals("Hello!", fake.lastMsg);
    }
}
