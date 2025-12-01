package org.example;

import org.example.service.EmailNotifier;
import org.example.service.EmailServer;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class EmailNotifierTest {

    @Test
    void testNotifierCallsServer() {
        EmailServer server = mock(EmailServer.class);
        EmailNotifier notifier = new EmailNotifier(server);
        notifier.notify("Ali", "Test Message");
        verify(server).sendEmail("Ali", "Test Message");
    }


}
