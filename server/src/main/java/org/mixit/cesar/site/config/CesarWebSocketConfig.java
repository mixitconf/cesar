package org.mixit.cesar.site.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Websocket configuration
 */
@Configuration
@EnableWebSocketMessageBroker
public class CesarWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * Configures the websocket message broker with the path "/topic".
     * Clients interested in the session ranking must subscribe to /topic'
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/ws");
    }

    /**
     * Configures the websocket endpoint /ranking with SockJS support.
     * Clients may connect using /api/ranking
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rankings").setAllowedOrigins("*").withSockJS();
    }

}
