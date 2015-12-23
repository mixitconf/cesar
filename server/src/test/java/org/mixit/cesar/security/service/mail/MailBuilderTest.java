package org.mixit.cesar.security.service.mail;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.velocity.VelocityEngineFactory;

/**
 * Test de {@link MailBuilder}
 */
public class MailBuilderTest {

    @Mock
    private AbsoluteUrlFactory urlFactory;

    @InjectMocks
    private MailBuilder mailBuilder = new MailBuilder();

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);

        VelocityEngineFactory factory = new VelocityEngineFactory();
        factory.setResourceLoaderPath("classpath:/templates/");

        mailBuilder.setVelocityEngine(factory.createVelocityEngine());
    }

    @Test(expected = NullPointerException.class)
    public void throw_exception_when_type_is_null() {
        mailBuilder.buildContent(null, new Account(), Optional.empty());
    }

    @Test(expected = NullPointerException.class)
    public void throw_exception_when_credentials_are_null() {
        mailBuilder.buildContent(MailBuilder.TypeMail.REINIT_PASSWORD, null, Optional.empty());
    }

    @Test
    public void generate_mail_for_reinit_password_with_token() {
        Mockito.when(urlFactory.getBaseUrl()).thenReturn("http://localhost:8080");
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.REINIT_PASSWORD, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://localhost:8080/app/account/password?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_for_cesar_account_validation_with_token() {
        Mockito.when(urlFactory.getBaseUrl()).thenReturn("http://localhost:8080");
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://localhost:8080/app/account/valid?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_for_social_account_validation_with_token() {
        Mockito.when(urlFactory.getBaseUrl()).thenReturn("http://localhost:8080");
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://localhost:8080/app/account/valid?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_in_english_if_default_language_is_en() {
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, new Account().setToken("MY-TOKEN-SECURED").setDefaultLanguage(SessionLanguage.en), Optional.empty()))
                .contains("Your credentials on Mix-IT website");
    }

    @Test
    public void generate_mail_in_french_by_default() {
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.SOCIAL_ACCOUNT_VALIDATION, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("Vos informations d'identification Mix-IT");
    }
}