package org.mixit.cesar.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;
import javax.validation.Validator;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.Authority;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.service.authentification.Credentials;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test {@link AccountService}
 */
public class AccountServiceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    public Validator validator;

    @Mock
    public MailerService mailerService;

    @Mock
    public MailBuilder mailBuilder;

    @InjectMocks
    public AccountService accountService;

    @Test(expected = NullPointerException.class)
    public void generateNewToken_should_not_generate_token_when_account_is_null() {
        accountService.generateNewToken(null);
    }

    @Test(expected = NullPointerException.class)
    public void generateNewToken_should_not_generate_token_when_oauthid_is_null() {
        accountService.generateNewToken(new Account());
    }

    @Test
    public void generateNewToken_should_generate_token() {
        Account account = new Account().setOauthId("anOAUTHID");
        accountService.generateNewToken(account);

        assertThat(account.getToken()).isNotEmpty();
    }

    @Test
    public void generateNewToken_should_generate_an_expiration_date_in_3_hour() {
        Account account = new Account().setOauthId("anOAUTHID");
        accountService.generateNewToken(account);

        assertThat(account.getTokenExpiration()).isNotNull().isAfter(LocalDateTime.now().minus(Duration.ofDays(3)));
    }

    @Test(expected = NullPointerException.class)
    public void createSocialAccount_should_not_create_social_account_when_provider_is_null() {
        accountService.createSocialAccount(null, "anOAUTHID");
    }

    @Test(expected = NullPointerException.class)
    public void createSocialAccount_should_not_create_social_account_when_oauthid_is_null() {
        accountService.createSocialAccount(OAuthProvider.GOOGLE, null);
    }


    Function<Account, Account> cloneAccount = account -> new Account()
            .setId(account.getId())
            .setToken(account.getToken())
            .setAuthorities(account.getAuthorities())
            .setValid(account.isValid());

    @Test
    public void createSocialAccount_should_create_social_account_but_not_valid() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(accountService.createSocialAccount(OAuthProvider.GOOGLE, "anOAUTHID").isValid()).isFalse();
    }

    /**
     * Token is sent in the second time by the provider
     */
    @Test
    public void createSocialAccount_should_create_social_account_but_without_token() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(accountService.createSocialAccount(OAuthProvider.GOOGLE, "anOAUTHID").getToken()).isNull();
    }

    @Test
    public void createSocialAccount_should_create_social_account_with_default_member_role() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> cloneAccount.apply(invocation.getArgumentAt(0, Account.class)));
        assertThat(accountService.createSocialAccount(OAuthProvider.GOOGLE, "anOAUTHID").getAuthorities()).extracting("name").containsExactly(Role.MEMBER);
    }

    @Test
    public void createNormalAccount_should_generate_oauthid() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        assertThat(accountService.createNormalAccount(new Account()).getOauthId()).isNotEmpty();
    }

    @Test
    public void createNormalAccount_should_return_a_token_null_to_user() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        assertThat(accountService.createNormalAccount(new Account()).getToken()).isNull();
    }

    @Test(expected = EmailExistException.class)
    public void createNormalAccount_should_throw_UserExistException_when_mail_already_exists() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Arrays.asList(new Member()));
        accountService.createNormalAccount(new Account());
    }

    @Test(expected = EmailExistException.class)
    public void createNormalAccount_should_throw_UserExistException_when_login_already_exists() {
        when(accountRepository.findByLogin(anyString())).thenReturn(new Account());
        accountService.createNormalAccount(new Account());
    }


    @Test
    public void createNormalAccount_should_send_mail_to_confirm_validation() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        accountService.createNormalAccount(new Account());
        verify(mailBuilder).createHtmlMail(any(MailBuilder.TypeMail.class), any(Credentials.class));
    }


}