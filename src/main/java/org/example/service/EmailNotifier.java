package org.example.service;
public class EmailNotifier implements Notifier {
    private final EmailServer server;

    public EmailNotifier(EmailServer server) {
        this.server = server;
    }

    @Override
    public void notify(String to, String message) {
        server.sendEmail(to, message);
    }
}
