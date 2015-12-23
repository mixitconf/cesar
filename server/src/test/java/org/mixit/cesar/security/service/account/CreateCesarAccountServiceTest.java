package org.mixit.cesar.security.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.Validator;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Authority;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.repository.AuthorityRepository;
import org.mixit.cesar.site.repository.MemberRepository;
import org.mixit.cesar.security.service.authentification.CryptoService;
import org.mixit.cesar.security.service.exception.EmailExistException;
import org.mixit.cesar.security.service.exception.LoginExistException;
import org.mixit.cesar.security.service.mail.MailBuilder;
import org.mixit.cesar.security.service.mail.MailerService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test {@link CreateCesarAccountService}
 */
public class CreateCesarAccountServiceTest {

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

    @Mock
    public CryptoService cryptoService;

    @Mock
    public TokenService tokenService;

    @InjectMocks
    public CreateCesarAccountService createCesarAccountService;


    @Test
    public void create_account_and_generate_oauthid() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
        assertThat(createCesarAccountService.createNormalAccount(new Account()).getOauthId()).isNotEmpty();
    }

    @Test
    public void create_account_and_return_a_token_null_to_user() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
        assertThat(createCesarAccountService.createNormalAccount(new Account()).getToken()).isNull();
    }

    @Test(expected = EmailExistException.class)
    public void throw_exception_when_mail_already_exists_on_account_creation() {
        when(tokenService.tryToLinkWithActualMember(any(Account.class))).thenThrow(EmailExistException.class);
        createCesarAccountService.createNormalAccount(new Account());
    }

    @Test(expected = LoginExistException.class)
    public void throw_exception_when_login_already_exists_on_account_creation() {
        when(accountRepository.findByLogin(anyString())).thenReturn(new Account());
        createCesarAccountService.createNormalAccount(new Account());
    }

    @Test
    public void create_account_send_mail_to_confirm_validation() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
        createCesarAccountService.createNormalAccount(new Account());
        verify(mailBuilder).buildContent(any(MailBuilder.TypeMail.class), any(Account.class), any());
    }

}