package org.example.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.example.domain.Book.logger;

public class FakeEmailServer implements EmailServer {

    private static final String OUTBOX_FILE = "emails.log";

    @Override
    public void sendEmail(String to, String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTBOX_FILE, true))) {
            bw.write("TO: " + to + " | MSG: " + message);
            bw.newLine();
        } catch (IOException e) {
            logger.severe("Error writing email: " + e.getMessage());
        }
    }
}
