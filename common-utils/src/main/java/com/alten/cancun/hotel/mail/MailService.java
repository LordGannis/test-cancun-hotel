package com.alten.cancun.hotel.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getFrom(), "Cancun Hotel"));
            mimeMessageHelper.setTo(mail.getTo());

            String fileName = "email/email.html";

            System.out.println("getResourceAsStream : " + fileName);
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream(fileName);

            String emailTemplate = "";
            try (InputStreamReader streamReader =
                         new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    emailTemplate += line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            emailTemplate = emailTemplate.replace("$title", mail.getSubject());
            emailTemplate = emailTemplate.replace("$user", mail.getUser());
            emailTemplate = emailTemplate.replace("$startDate", mail.getStartDate());
            emailTemplate = emailTemplate.replace("$endDate", mail.getEndDate());

            mimeMessageHelper.setText(emailTemplate, true);

            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
