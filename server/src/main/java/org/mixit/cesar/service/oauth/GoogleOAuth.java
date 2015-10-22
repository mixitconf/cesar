package org.mixit.cesar.service.oauth;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import org.mixit.cesar.model.security.OAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of OAuth for Google. Implements the protocol described
 * <a href="https://developers.google.com/accounts/docs/OAuth2Login">in the Google documentation</a>.
 * Actually uses OpenID/Connect.
 *
 * @author JB Nizet
 */
@Component
public class GoogleOAuth extends AbstractOAuth2 {

    private static final String GOOGLE_STATE_ATTRIBUTE = GoogleOAuth.class.getName() + "-state";

    @Value("${oauth.google.apiKey}")
    private String apiKey;

    @Value("${oauth.google.clientSecret}")
    private String clientSecret;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    protected String getStep1Url() {
        return "https://accounts.google.com/o/oauth2/auth";
    }

    @Override
    protected String getApiKey() {
        return apiKey;
    }

    @Override
    protected String getClientSecret() {
        return clientSecret;
    }

    @Override
    protected String getStateSessionAttribute() {
        return GOOGLE_STATE_ATTRIBUTE;
    }

    @Override
    protected String getScopes() {
        return "openid";
    }

    @Override
    protected String getStep2Url() {
        return "https://accounts.google.com/o/oauth2/token";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String getOAuthId(Map<String, ?> oauthResponse) throws IOException {
        String jwt = (String) oauthResponse.get("id_token");
        String encodedUserInformation = Splitter.on('.').splitToList(jwt).get(1);
        byte[] json = BaseEncoding.base64Url().decode(encodedUserInformation);

        Map<String, ?> userInformation = objectMapper.readValue(json, Map.class);

        return (String) userInformation.get("sub");
    }
}
