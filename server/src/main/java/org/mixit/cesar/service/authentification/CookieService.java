package org.mixit.cesar.service.authentification;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.exception.ExpiredTokenException;
import org.mixit.cesar.service.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CookieService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create token if it does'nt exist
     */
    public Credentials setCookieInResponse(HttpServletResponse response, Account account) {
        if (account.getToken() == null) {
            account.setToken(UUID.randomUUID().toString());
            accountRepository.save(account);
        }

        Cookie cookie = new Cookie(AuthenticationInterceptor.TOKEN_COOKIE_NAME, account.getToken());
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).getSeconds());
        response.addCookie(cookie);
        return Credentials.build(account);
    }

    /**
     * Delete token in response
     */
    public void deleteCookieInResponse(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthenticationInterceptor.TOKEN_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).getSeconds());
        response.addCookie(cookie);
    }

}
