package org.mixit.cesar.service.account;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.exception.ExpiredTokenException;
import org.mixit.cesar.service.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Generate a new token
     */
    public void generateNewToken(Account account) {
        Objects.requireNonNull(account);
        Objects.requireNonNull(account.getOauthId());
        account.setToken(UUID.randomUUID().toString());
        reinitTokenValidity(account);
    }


    /**
     * Reinit token validity
     */
    public void reinitTokenValidity(Account account) {
        Objects.requireNonNull(account);
        account.setTokenExpiration(LocalDateTime.now().plus(Duration.ofHours(3)));
    }

    /**
     * Chek user with token
     */
    public Account checkToken(String token) {
        Account account = accountRepository.findByToken(token);

        //Step1: we check the token and its validity
        if (account == null) {
            throw new InvalidTokenException();
        }
        //Step2: the token is only valid during 3 hours
        if (account.getTokenExpiration() == null || LocalDateTime.now().minus(Duration.ofHours(3)).compareTo(account.getTokenExpiration()) > 0) {
            throw new ExpiredTokenException();
        }

        return account;
    }


    /**
     * Check user and return credentials
     */
    public Account getCredentialsForToken(String token) {
        Account account = checkToken(token);
        account.setValid(true);
        return account;
    }
}
