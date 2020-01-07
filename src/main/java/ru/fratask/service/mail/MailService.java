package ru.fratask.service.mail;

import java.util.List;

public interface MailService {

    /**
     * Send personal email message
     *
     * @param to    email receiver
     * @param title email title
     * @param body  email body
     */
    void send(String to, String title, String body);

    /**
     * Send email message for many email addresses
     *
     * @param to    List of email addresses
     * @param title email title
     * @param body  email body
     */
    void send(List<String> to, String title, String body);
}
