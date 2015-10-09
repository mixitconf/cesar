package org.mixit.cesar.security.authentification;

import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Current user bean sored in the request scope
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class CurrentUser {

    private Credentials credentials;

    public Optional<Credentials> getCredentials() {
        return Optional.ofNullable(credentials);
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}