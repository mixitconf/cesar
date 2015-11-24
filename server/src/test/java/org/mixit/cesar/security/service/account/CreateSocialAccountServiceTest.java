package org.mixit.cesar.security.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Authority;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test {@link CreateSocialAccountService}
 */
public class CreateSocialAccountServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    public CreateSocialAccountService createSocialAccountService;


    /**
     * Function used to simulate account creation. The repository return a new instance of an object
     */
    Function<Account, Account> cloneAccount = account -> new Account()
            .setId(account.getId())
            .setToken(account.getToken())
            .setAuthorities(account.getAuthorities())
            .setValid(account.isValid());


    @Test(expected = NullPointerException.class)
    public void throw_exception_when_provider_is_null_on_social_account_creation() {
        createSocialAccountService.createAccount(null, "anOAUTHID");
    }

    @Test(expected = NullPointerException.class)
    public void throw_exception_when_oauthid_is_null_on_social_account_creation() {
        createSocialAccountService.createAccount(OAuthProvider.GOOGLE, null);
    }

    @Test
    public void create_social_account_but_not_valid() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(createSocialAccountService.createAccount(OAuthProvider.GOOGLE, "anOAUTHID").isValid()).isFalse();
    }

    /**
     * Token is sent in the second time by the provider
     */
    @Test
    public void create_social_account_but_without_token() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(createSocialAccountService.createAccount(OAuthProvider.GOOGLE, "anOAUTHID").getToken()).isNull();
    }

    @Test
    public void create_social_account_with_default_member_role() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(createSocialAccountService.createAccount(OAuthProvider.GOOGLE, "anOAUTHID").getAuthorities()).extracting("name").containsExactly(Role.MEMBER);
    }


}