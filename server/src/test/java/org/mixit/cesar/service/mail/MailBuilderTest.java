package org.mixit.cesar.service.mail;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.authentification.Credentials;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Test de {@link MailBuilder}
 */
public class MailBuilderTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private AbsoluteUrlFactory urlFactory;

    @InjectMocks
    MailBuilder mailBuilder;

    @Test(expected = NullPointerException.class)
    public void throw_exception_when_type_is_null(){
        mailBuilder.createHtmlMail(null, new Credentials(), Optional.empty());
    }

    @Test(expected = NullPointerException.class)
    public void throw_exception_when_credentials_are_null(){
        mailBuilder.createHtmlMail(MailBuilder.TypeMail.REINIT_PASSWORD, null, Optional.empty());
    }

    @Test
    public void generate_mail_for_reinit_password_with_token(){
        when(urlFactory.getBaseUrl()).thenReturn("http://mix-it");
        assertThat(mailBuilder.createHtmlMail(MailBuilder.TypeMail.REINIT_PASSWORD, new Credentials().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://mix-it/app/account/password?token=MY-TOKEN-SECURED");
   }

    @Test
    public void generate_mail_for_cesar_account_validation_with_token(){
        when(urlFactory.getBaseUrl()).thenReturn("http://mix-it");
        assertThat(mailBuilder.createHtmlMail(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, new Credentials().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://mix-it/app/account/valid?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_for_social_account_validation_with_token(){
        when(urlFactory.getBaseUrl()).thenReturn("http://mix-it");
        assertThat(mailBuilder.createHtmlMail(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, new Credentials().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://mix-it/app/account/valid?token=MY-TOKEN-SECURED");
    }
}