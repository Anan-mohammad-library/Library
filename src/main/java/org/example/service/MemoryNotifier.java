package org.example.service;

import java.util.*;

public class MemoryNotifier implements Notifier {

    private final Map<String, List<String>> messages = new HashMap<>();

    @Override
    public void notify(String to, String message) {
        messages.computeIfAbsent(to, k -> new ArrayList<>()).add(message);
    }

    public List<String> getMessages(String user) {
        return messages.getOrDefault(user, new ArrayList<>());
    }

    public void clearMessages(String user) {
        messages.remove(user);
    }
}
