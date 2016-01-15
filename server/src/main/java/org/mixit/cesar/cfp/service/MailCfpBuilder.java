package org.mixit.cesar.cfp.service;

import java.util.HashMap;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.apache.velocity.app.VelocityEngine;
import org.mixit.cesar.cfp.model.Proposal;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.site.model.session.SessionLanguage;
import org.mixit.cesar.site.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailCfpBuilder {

    @Autowired
    private VelocityEngine velocityEngine;


    public enum TypeMail {
        SESSION_SUBMITION,
        SESSION_REJECTED,
        SESSION_ACCEPTED
    }

    public String buildContent(TypeMail typeMail, Account account, Proposal proposal) {
        Preconditions.checkNotNull(typeMail, "type is required");
        Preconditions.checkNotNull(account, "credentials are required");

        String lang = account.getDefaultLanguage().equals(SessionLanguage.en) ? "en" : "fr";

        Map<String, Object> model = new HashMap<>();
        model.put("account", account);
        model.put("proposal", proposal);

        switch (typeMail) {
            case SESSION_SUBMITION:
                return VelocityEngineUtils.mergeTemplateIntoString(
                        this.velocityEngine,
                        String.format("email-session-submit-%s.vm", lang),
                        "UTF-8",
                        model);
            default:
                return null;
        }
    }

    public String getTitle(TypeMail typeMail) {
        Preconditions.checkNotNull(typeMail, "type is required");

        String title;

        switch (typeMail) {
            case SESSION_ACCEPTED:
            case SESSION_REJECTED:
            case SESSION_SUBMITION:
                title = "CFP Mix-IT " + EventService.getCurrent().getYear();
                break;

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
