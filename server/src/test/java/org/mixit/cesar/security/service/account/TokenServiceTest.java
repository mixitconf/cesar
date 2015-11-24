package org.mixit.cesar.security.service.account;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.exception.ExpiredTokenException;
import org.mixit.cesar.security.service.exception.InvalidTokenException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test {@link TokenService}
 */
public class TokenServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TokenService tokenService;


    @Test(expected = NullPointerException.class)
    public void should_not_generate_token_when_account_is_null() {
        tokenService.generateNewToken(null);
    }

    @Test(expected = NullPointerException.class)
    public void should_not_generate_token_when_oauthid_is_null() {
        tokenService.generateNewToken(new Account());
    }

    @Test
    public void should_generate_token() {
        Account account = new Account().setOauthId("anOAUTHID");
        tokenService.generateNewToken(account);

        assertThat(account.getToken()).isNotEmpty();
    }

    @Test
    public void should_generate_token_with_an_expiration_date_in_3_hour() {
        Account account = new Account().setOauthId("anOAUTHID");
        tokenService.generateNewToken(account);

        assertThat(account.getTokenExpiration()).isNotNull().isAfter(LocalDateTime.now().minus(Duration.ofDays(3)));
    }


    @Test(expected = NullPointerException.class)
    public void should_not_reinit_token_validity_when_account_is_null() {
        tokenService.reinitTokenValidity(null);
    }

    @Test
    public void should_reinit_token_expiration_date() {
        Account account = new Account().setOauthId("anOAUTHID");
        tokenService.reinitTokenValidity(account);

        assertThat(account.getTokenExpiration()).isNotNull().isAfter(LocalDateTime.now().minus(Duration.ofDays(3)));
    }

    @Test
    public void should_check_token() {
        when(accountRepository.findByToken("token")).thenReturn(new Account().setTokenExpiration(LocalDateTime.now().plus(Duration.ofHours(1))));
        assertThat(tokenService.checkToken("token")).isNotNull();
    }

    @Test(expected = ExpiredTokenException.class)
    public void should_not_check_token_when_token_expiration_date_null() {
        when(accountRepository.findByToken("token")).thenReturn(new Account());
        tokenService.checkToken("token");
    }

    @Test(expected = InvalidTokenException.class)
    public void should_not_check_token_when_no_account_for_token() {
        when(accountRepository.findByToken("token")).thenReturn(null);
        tokenService.checkToken("token");
    }

    @Test(expected = ExpiredTokenException.class)
    public void should_not_check_token_when_no_expiration_date_expired() {
        when(accountRepository.findByToken("token")).thenReturn(new Account().setTokenExpiration(LocalDateTime.now().minus(Duration.ofHours(4))));
        tokenService.checkToken("token");
    }

    @Test
    public void should_get_credentials_for_token() {
        Account account = new Account().setTokenExpiration(LocalDateTime.now().plus(Duration.ofHours(1)));
        when(accountRepository.findByToken("token")).thenReturn(account);

        assertThat(account.isValid()).isFalse();
        assertThat(tokenService.getCredentialsForToken("token")).isNotNull();
        assertThat(account.isValid()).isTrue();
    }

}