package org.mixit.cesar.security.service.mail;

import java.util.Optional;

import com.google.common.base.Preconditions;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.OAuthProvider;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailBuilder {

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    public enum TypeMail {
        REINIT_PASSWORD,
        ACCOUND_NEW_VALIDATION,
        SOCIAL_ACCOUNT_VALIDATION,
        CESAR_ACCOUNT_VALIDATION,
        EMAIL_CHANGED
    }

    public String createHtmlMail(TypeMail typeMail, Account account, Optional<OAuthProvider> provider) {
        Preconditions.checkNotNull(typeMail, "type is required");
        Preconditions.checkNotNull(account, "credentials are required");

        StringBuilder message = new StringBuilder();

        message.append("<div style=\"font-family: Arial;color: #424242;margin:2em\">");
        message.append("<p>Bonjour <b>").append(account.getFirstname()).append(" ").append(account.getLastname()).append("</b></p>");
        message.append("<h2>Vos informations d'identification Mix-IT</h2>");

        //TODO i18n
        String url = String.format("%s/app/account/valid?token=%s", urlFactory.getBaseUrl(), account.getToken());
        switch (typeMail) {
            case REINIT_PASSWORD:
                url = String.format("%s/app/account/password?token=%s", urlFactory.getBaseUrl(), account.getToken());
                message.append("<p>Vous nous avez demand&nbsp; de r&nbsp;initialiser votre mot de passe. Pour celà veuillez suivre le lien suivant <a href=\"")
                        .append(url)
                        .append("\">")
                        .append(url)
                        .append("</a></p><p>Sur cette page vous devrez renseigner ce mot de passe provisoire <b>")
                        .append(account.getPassword())
                        .append("</b> pour pouvoir en d&nbsp;finir un nouveau. ")
                        .append("Si vous n'êtes pas à l'origine de cette action veuillez nous tenir informer</p>");
                break;
            case EMAIL_CHANGED:
                url = String.format("%s/app/account/password?token=%s", urlFactory.getBaseUrl(), account.getToken());
                message.append("<p>Vous venez de changer votre adresse email pour utiliser ")
                        .append(account.getEmail())
                        .append(". Pour celà veuillez suivre le lien suivant <a href=\"")
                        .append(url)
                        .append("\">")
                        .append(url)
                        .append("</a></p>");
                break;
            case ACCOUND_NEW_VALIDATION:
                message.append("<p>Vous nous avez demand&nbsp; de r&nbsp;initialiser votre mot de passe mais vous utilisez une connexion via le reseau social ")
                        .append(provider.orElse(OAuthProvider.TWITTER))
                        .append(". Nous ne pouvons pas changer le mot de passe. Si vous souhaitez à nouveau valider votre adresse email cliquez sur le lien suivant <a href=\"")
                        .append(url)
                        .append("\">")
                        .append(url)
                        .append("</a></p>");
                break;
            case CESAR_ACCOUNT_VALIDATION:
            case SOCIAL_ACCOUNT_VALIDATION:
                message.append("<p>Vous venez de cr&nbsp;er un compte sur le site de <a href=\"")
                        .append(urlFactory.getBaseUrl())
                        .append("\">Mix-IT</a>. Pour le valider veuillez cliquer sur ce lien <a href=\"")
                        .append(url)
                        .append("\">")
                        .append(url)
                        .append("</a>. Vous pourrez ensuite utiliser la partie s&nbsp;curis&nbsp;e du site.</p>");
                break;
        }

        message.append("<p>Attention, ce code n'est valable que pendant trois heures. Pass&nbsp; ce d&nbsp;lai, vous devrez soumettre une nouvelle demande de modification de vos informations d'identification.</p>");
        message.append("<b>La team Mix-IT</b>");
        message.append("</div>");

        return message.toString();
    }

}
