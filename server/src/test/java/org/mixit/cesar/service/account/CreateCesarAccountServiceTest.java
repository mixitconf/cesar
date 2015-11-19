package org.mixit.cesar.service.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import javax.validation.Validator;

import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.Authority;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.repository.MemberRepository;
import org.mixit.cesar.service.authentification.CryptoService;
import org.mixit.cesar.service.exception.EmailExistException;
import org.mixit.cesar.service.exception.LoginExistException;
import org.mixit.cesar.service.mail.MailBuilder;
import org.mixit.cesar.service.mail.MailerService;
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
        assertThat(createCesarAccountService.createNormalAccount(new Account()).getOauthId()).isNotEmpty();
    }

    @Test
    public void create_account_and_return_a_token_null_to_user() {
        when(authorityRepository.findByName(Role.MEMBER)).thenReturn(new Authority().setId(1L).setName(Role.MEMBER));
        assertThat(createCesarAccountService.createNormalAccount(new Account()).getToken()).isNull();
    }

    @Test(expected = EmailExistException.class)
    public void throw_exception_when_mail_already_exists_on_account_creation() {
        when(tokenService.tryToLinkWithActualMember(anyString())).thenThrow(EmailExistException.class);
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
        createCesarAccountService.createNormalAccount(new Account());
        verify(mailBuilder).createHtmlMail(any(MailBuilder.TypeMail.class), any(Account.class), any());
    }

}