package org.example;

import org.example.service.MemoryNotifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryNotifierTest {

    @Test
    void storeAndRetrieveMessages() {
        MemoryNotifier notifier = new MemoryNotifier();
        notifier.notify("Ali", "Hi");
        assertEquals(1, notifier.getMessages("Ali").size());
    }

    @Test
    void clearMessagesRemovesData() {
        MemoryNotifier notifier = new MemoryNotifier();
        notifier.notify("Ali", "Msg");
        notifier.clearMessages("Ali");
        assertTrue(notifier.getMessages("Ali").isEmpty());
    }
}
