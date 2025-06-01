package com.example.gestionuser;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class GmailTest {
    public static void main(String[] args) {
        final String username = "sport.sync.contact.pi2025@gmail.com";
        final String password = "gmfxifxkblfcdxtk"; // App password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    @Override
                    protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new jakarta.mail.PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("sport.sync.contact.pi2025@gmail.com")
            );
            message.setSubject("Test Mail from Java Console");
            message.setText("✅ Hello! This is a test email sent from a standalone Java app.");

            Transport.send(message);

            System.out.println("✅ Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
