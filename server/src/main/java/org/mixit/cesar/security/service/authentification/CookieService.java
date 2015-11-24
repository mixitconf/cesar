package org.mixit.cesar.security.service.authentification;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CookieService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create token if it does'nt exist
     */
    public void setCookieInResponse(HttpServletResponse response, Account account) {
        if (account.getToken() == null) {
            account.setToken(UUID.randomUUID().toString());
            accountRepository.save(account);
        }

        Cookie cookie = new Cookie(AuthenticationFilter.TOKEN_COOKIE_NAME, account.getToken());
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).getSeconds());
        response.addCookie(cookie);
    }

    /**
     * Delete token in response
     */
    public void deleteCookieInResponse(HttpServletResponse response) {
        Cookie cookie = new Cookie(AuthenticationFilter.TOKEN_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).getSeconds());
        response.addCookie(cookie);
    }

}
