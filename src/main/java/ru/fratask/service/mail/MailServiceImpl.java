package ru.fratask.service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(String to, String title, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(body);
        javaMailSender.send(message);
    }

    @Override
    public void send(List<String> to, String title, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(String.valueOf(to));
        message.setSubject(title);
        message.setText(body);
        javaMailSender.send(message);
    }
}
