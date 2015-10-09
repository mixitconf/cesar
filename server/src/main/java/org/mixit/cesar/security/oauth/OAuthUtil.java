package org.mixit.cesar.security.oauth;

import org.mixit.cesar.model.security.OAuthProvider;

/**
 * Utility methods to deal with requests.
 *
 * @author JB Nizet
 */
public final class OAuthUtil {
    private OAuthUtil() {
    }

    /**
     * Returns the redirect URL for the given provider. If the request is
     * http://localhost:8080/foo/bar?baz=zing and the provider is GOOGLE, this method returns
     * http://localhost:8080/oauth/google.
     */
    public static String getRedirectUrl(String baseRedirectUrl, OAuthProvider provider) {
        return baseRedirectUrl + "/app/oauth/" + provider.name().toLowerCase();
    }
}
