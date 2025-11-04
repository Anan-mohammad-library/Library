package org.example.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class JakartaEmailServer implements EmailServer {

    private final Dotenv dotenv;

    public JakartaEmailServer() {
        dotenv = Dotenv.load();
    }

    @Override
    public void sendEmail(String to, String message) {
        String fromEmail = dotenv.get("EMAIL_USER");
        String password = dotenv.get("EMAIL_PASS");

        if (fromEmail == null || password == null) {
            throw new RuntimeException("EMAIL_USER and EMAIL_PASS must be set in .env file!");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Library Overdue Reminder");
            msg.setText(message);

            Transport.send(msg);
            System.out.println("✅ Email sent to " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }
}
