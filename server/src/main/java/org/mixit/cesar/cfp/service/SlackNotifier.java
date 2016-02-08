package org.mixit.cesar.cfp.service;

import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mixit.cesar.cfp.model.SlackMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dev-Mind <guillaume@dev-mind.fr>
 * @since 29/01/16.
 */
@Service
public class SlackNotifier {

    private static final Logger logger = LoggerFactory.getLogger(SlackNotifier.class);

    public enum SlackChannel {
        cfp,
        general
    }

    @Value("${cesar.slack}")
    private String slackUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public void send(String message, SlackChannel channel) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        SlackMessage slackMessage = new SlackMessage()
                .setChannel(channel.toString())
                .setText(message)
                .setUsername("Cesar")
                .setIcon_emoji("computer");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(slackMessage), headers);
            restTemplate.exchange(slackUrl, HttpMethod.POST, request, String.class);
        }
        catch (RuntimeException | JsonProcessingException e) {
            logger.error("Error whe we try to contact Slack ", e);
        }
    }
}
