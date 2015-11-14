package org.mixit.cesar.service.authentification;

import java.util.Optional;

import org.mixit.cesar.model.security.Account;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Current user bean sored in the request scope
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class CurrentUser {

    private Account account;

    public Optional<Account> getCredentials() {
        return Optional.ofNullable(account);
    }

    public void setCredentials(Account account) {
        this.account = account;
    }
}