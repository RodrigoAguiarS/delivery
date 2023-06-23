package com.rodrigo.delivery.service;

import com.rodrigo.delivery.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class PasswordResetService implements EmailService {
    @Value("${email.username}")
    private String emailUsername;
    @Value("${email.password}")
    private String emailPassword;

    /**
     * Envia um email para o destinatário especificado.
     *
     * @param recipientEmail o endereço de email do destinatário
     * @param subject o assunto do email
     * @param content o conteúdo do email
     */
    @Override
    public void sendEmail(String recipientEmail, String subject, String content) {
        // Configurar as propriedades do servidor de email
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com"); // Endereço do servidor SMTP do Outlook/Office 365
        props.put("mail.smtp.port", "587"); // Porta do servidor SMTP do Outlook/Office 365
        props.put("mail.smtp.auth", "true"); // Habilitar autenticação
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS

        // Configurar as credenciais de autenticação do remetente
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        };

        // Criar uma sessão de email
        Session session = Session.getInstance(props, auth);

        try {
            // Criar uma mensagem de email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("rodrigo_aguiars@outlook.com")); // Endereço de email do remetente
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Endereço de email do destinatário
            message.setSubject(subject); // Assunto do email
            message.setText(content); // Conteúdo do email

            // Enviar o email
            Transport.send(message);
            System.out.println("Email enviado com sucesso.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar o email: " + e.getMessage());
        }
    }
}

