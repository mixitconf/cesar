package org.mixit.cesar.security.service.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.apache.velocity.app.VelocityEngine;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailBuilder {

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    private VelocityEngine velocityEngine;

    public enum TypeMail {
        REINIT_PASSWORD,
        ACCOUND_NEW_VALIDATION,
        SOCIAL_ACCOUNT_VALIDATION,
        CESAR_ACCOUNT_VALIDATION,
        EMAIL_CHANGED,
        SESSION_SUBMITION,
        SESSION_REJECTED,
        SESSION_ACCEPTED
    }

    public String buildContent(TypeMail typeMail, Account account, Optional<OAuthProvider> provider) {
        Preconditions.checkNotNull(typeMail, "type is required");
        Preconditions.checkNotNull(account, "credentials are required");

        String lang = account.getDefaultLanguage().equals(SessionLanguage.en) ? "en" : "fr";

        Map<String, Object> model = new HashMap<>();
        model.put("account", account);

        switch (typeMail) {
            case REINIT_PASSWORD:
                model.put("url", String.format("%s/app/account/password?token=%s", urlFactory.getBaseUrl(), account.getToken()));
                return VelocityEngineUtils.mergeTemplateIntoString(
                        this.velocityEngine,
                        String.format("email-reinit-password-%s.vm", lang),
                        "UTF-8",
                        model);

            case EMAIL_CHANGED:
                model.put("url", String.format("%s/app/account/password?token=%s", urlFactory.getBaseUrl(), account.getToken()));
                return VelocityEngineUtils.mergeTemplateIntoString(
                        this.velocityEngine,
                        String.format("email-change-email-%s.vm", lang),
                        "UTF-8",
                        model);

            case ACCOUND_NEW_VALIDATION:
                model.put("provider", provider.orElse(OAuthProvider.TWITTER).toString());
                model.put("url", String.format("%s/app/account/valid?token=%s", urlFactory.getBaseUrl(), account.getToken()));
                return VelocityEngineUtils.mergeTemplateIntoString(
                        this.velocityEngine,
                        String.format("email-account-validation-%s.vm", lang),
                        "UTF-8",
                        model);

            case CESAR_ACCOUNT_VALIDATION:
            case SOCIAL_ACCOUNT_VALIDATION:
                model.put("url", String.format("%s/app/account/valid?token=%s", urlFactory.getBaseUrl(), account.getToken()));
                return VelocityEngineUtils.mergeTemplateIntoString(
                        this.velocityEngine,
                        String.format("email-account-new-%s.vm", lang),
                        "UTF-8",
                        model);

            case SESSION_ACCEPTED:
            case SESSION_REJECTED:
            case SESSION_SUBMITION:
                return "<p>TODO à faire et à gérer correctement ces mails";
        }

        return null;
    }

    public String getTitle(TypeMail typeMail, Account account) {
        Preconditions.checkNotNull(typeMail, "type is required");
        Preconditions.checkNotNull(account, "credentials are required");

        String lang = account.getDefaultLanguage().equals(SessionLanguage.en) ? "en" : "fr";
        String title;

        switch (typeMail) {
            case REINIT_PASSWORD:
                title = lang.equals("en") ? "Password reinitialization" : "Réinitialisation de votre mot de passe";
                break;

            case EMAIL_CHANGED:
                title = lang.equals("en") ? "Email changed" : "Modification de votre mot de passe";
                break;

            case CESAR_ACCOUNT_VALIDATION:
            case SOCIAL_ACCOUNT_VALIDATION:
            case ACCOUND_NEW_VALIDATION:
                title = lang.equals("en") ? "Account validation" : "Validation de votre compte";
                break;

            case SESSION_ACCEPTED:
            case SESSION_REJECTED:
            case SESSION_SUBMITION:
                title = "Session on Mix-IT CFP";

            default:
                throw new IllegalArgumentException();
        }

        return "[Mix-IT] " + title;
    }

    @VisibleForTesting
    protected void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
