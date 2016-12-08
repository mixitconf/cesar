package org.mixit.cesar.security.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class CesarMailConfig {

    @Value("${cesar.mail.smtp.auth}")
    private String smtpAuth;
    @Value("${cesar.mail.smtp.starttls}")
    private String smtpStartTls;
    @Value("${cesar.mail.smtp.host}")
    private String smtpHost;
    @Value("${cesar.mail.smtp.port}")
    private String smtpPort;
    @Value("${cesar.mail.smtp.user}")
    private String smtpUser;
    @Value("${cesar.mail.smtp.password}")
    private String smtpPassword;

    @Bean
    public JavaMailSender mailSender() {
        Properties properties = new Properties();
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", smtpStartTls);

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(smtpHost);
        sender.setUsername(smtpUser);
        sender.setPassword(smtpPassword);

        sender.setJavaMailProperties(properties);
        return sender;
    }
}
