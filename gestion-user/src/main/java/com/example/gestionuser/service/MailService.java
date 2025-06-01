package com.example.gestionuser.service;

public interface MailService {
    void sendEmail(String to, String subject, String text);
}
