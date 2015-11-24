package org.mixit.cesar.security.service.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Created by ehret_g on 27/10/15.
 */
@Service
public class MailerService {


    private static final Logger logger = LoggerFactory.getLogger(MailerService.class);

    @Autowired
    private JavaMailSender mailSender;


    public void send(String to, String subject, String html) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            message.setContent(html, "text/html");
            helper.setTo(to);
            helper.setSubject(subject);

            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error(String.format("Not possible to send email [%s] to %s", subject, to), e);
            throw new RuntimeException("Error when system send the mail " + subject, e);
        }

    }
}
