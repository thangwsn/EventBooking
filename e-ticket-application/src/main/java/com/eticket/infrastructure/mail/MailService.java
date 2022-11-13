package com.eticket.infrastructure.mail;

import com.eticket.domain.entity.event.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);
    @Value("${client.host}")
    private String clientHost;
    @Value("${mail.username}")
    private String sender;
    @Value("${mail.password}")
    private String machinePassword;

    @Async
    public void sendVerificationMail(Integer userId, String userName, String email, String code) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, machinePassword);
            }
        });

        Message message = prepareVerifyMessage(session, sender, email, userId, userName, code);
        try {
            Transport.send(message);
            logger.info("Mail sent successfully.");
        } catch (MessagingException e) {
            logger.error("Error while sending mail---{}", e.getMessage());
        }
    }

    @Async
    public void sendMailAttachment(String userName, String email, List<Ticket> listTicket) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, machinePassword);
            }
        });

        Message message = prepareMessageAttachment(session, sender, email, userName, listTicket);
        try {
            Transport.send(message);
            logger.info("Mail sent successfully.");
        } catch (MessagingException e) {
            logger.error("Error while sending mail---{}", e.getMessage());
        }
    }

    private Message prepareVerifyMessage(Session session, String from, String to, Integer userId, String userName, String code) {
        Message message = new MimeMessage(session);
        try {

            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("VERIFY YOUR REGISTRATION");
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "E-ticket Ltd.";
            content = content.replace("[[name]]", userName);
            String verifyURL = clientHost + "/verify-register?user_id=" + userId + "&active_code=" + code;

            content = content.replace("[[URL]]", verifyURL);
            message.setContent(content, "text/html");
            return message;
        } catch (AddressException e) {
            logger.error("Error while prepare mail---{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error while prepare mail---{}", e.getMessage());
        }
        return null;
    }

    private Message prepareMessageAttachment(Session session, String from, String to, String userName, List<Ticket> listTicket) {
        Message message = new MimeMessage(session);
        try {

            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("BOOKING SUCCESS");
            Multipart multipart = new MimeMultipart();
            // Now set the actual message
            String htmlText = "<h1>Dear <i> " + userName + "</i></h1>";
            message.setContent(htmlText, "text/html");
            for (Ticket ticket : listTicket) {
                BodyPart messageBodyPart = new MimeBodyPart();
                htmlText += "<img align=\" center \" src=\"cid:" + ticket.getCode() + "\"><br>";
                messageBodyPart.setContent(htmlText, "text/html");
                String filename = "src/main" + ticket.getQRcode();
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setHeader("Content-ID", "<" + ticket.getCode() + ">");
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            return message;
        } catch (AddressException e) {
            e.printStackTrace();
            logger.error("Error while prepare mail---{}", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while prepare mail---{}", e.getMessage());
        }
        return null;
    }
}
