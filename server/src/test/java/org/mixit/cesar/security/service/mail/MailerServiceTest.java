package org.mixit.cesar.security.service.mail;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;

import javax.mail.MessagingException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by ehret_g on 27/10/15.
 */
public class MailerServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public MailSender mailSender;

    @InjectMocks
    public MailerService mailerService;


    @Test(expected = RuntimeException.class)
    public void should_throwRunTimeException_when_mail_error() throws Exception {
        doThrow(MessagingException.class).when(mailSender).send(any(SimpleMailMessage.class));
        mailerService.send("to", "subject", "<h1>Hello</h1>");
    }
}