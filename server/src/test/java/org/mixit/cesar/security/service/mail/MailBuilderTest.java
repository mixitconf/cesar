package org.mixit.cesar.security.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mixit.cesar.CesarApplication;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Test de {@link MailBuilder}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CesarApplication.class)
@WebIntegrationTest(randomPort = true)
@DirtiesContext
public class MailBuilderTest {

    @Autowired
    MailBuilder mailBuilder;

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
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.REINIT_PASSWORD, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://localhost:8080/app/account/password?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_for_cesar_account_validation_with_token() {
        assertThat(mailBuilder.buildContent(MailBuilder.TypeMail.CESAR_ACCOUNT_VALIDATION, new Account().setToken("MY-TOKEN-SECURED"), Optional.empty()))
                .contains("http://localhost:8080/app/account/valid?token=MY-TOKEN-SECURED");
    }

    @Test
    public void generate_mail_for_social_account_validation_with_token() {
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