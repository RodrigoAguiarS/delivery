package com.rodrigo.delivery.service.impl;

public interface EmailService {
    void sendEmail(String recipientEmail, String subject, String content);
}
