package org.mixit.cesar.service.oauth;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.mixit.cesar.model.security.BadRequestException;
import org.mixit.cesar.model.security.OAuthProvider;

/**
 * The common interface of the OAuth authentications. Since it's a standard, a different implementation is
 * necessary for every provider.
 *
 * @author JB Nizet
 */
public interface OAuth {

    /**
     * Gets the provider of this OAuth service
     */
    OAuthProvider getProvider();

    /**
     * Starts the OAuth dance, stores some token or state in the session, and returns the URL where the user
     * must be redirected to authenticate.
     */
    String getRedirectUrl(HttpServletRequest request);

    /**
     * Gets the oauth ID from the callback request sent by the OAuth provider.
     *
     * @return the OAuth ID, or empty if the user refused to authenticate
     * @throws BadRequestException if the state or token saved in the first step is invalid
     */
    Optional<String> getOAuthId(HttpServletRequest request) throws BadRequestException, IOException;
}
