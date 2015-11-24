package org.mixit.cesar.security.service.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.google.common.io.BaseEncoding;
import org.mixit.cesar.security.model.BadRequestException;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Base class for OAuth2 implementations.
 *
 * @author JB Nizet
 */
public abstract class AbstractOAuth2 implements OAuth {

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;

    private SecureRandom secureRandom = new SecureRandom();

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        String state = generateCsrfToken();
        request.getSession().setAttribute(getStateSessionAttribute(), state);

        String redirectUri = OAuthUtil.getRedirectUrl(absoluteUrlFactory.getBaseUrl(), getProvider());

        return getStep1Url()
                + "?response_type=code&client_id="
                + encode(getApiKey())
                + "&redirect_uri="
                + encode(redirectUri)
                + "&scope="
                + encode(getScopes())
                + "&state="
                + encode(state);
    }

    @Override
    public Optional<String> getOAuthId(HttpServletRequest request) throws IOException, BadRequestException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code == null) {
            return Optional.empty();
        }

        if (!state.equals(request.getSession().getAttribute(getStateSessionAttribute()))) {
            throw new BadRequestException("invalid state");
        }

        return Optional.of(getOAuthId(code));
    }

    protected abstract String getStep1Url();

    protected abstract String getApiKey();

    protected abstract String getClientSecret();

    protected abstract String getStateSessionAttribute();

    protected abstract String getScopes();

    protected abstract String getStep2Url();

    protected abstract String getOAuthId(Map<String, ?> oauthResponse) throws IOException;

    @SuppressWarnings("unchecked")
    private String getOAuthId(String code) throws IOException {
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("client_id", getApiKey());
        parameters.add("client_secret", getClientSecret());
        parameters.add("redirect_uri", OAuthUtil.getRedirectUrl(absoluteUrlFactory.getBaseUrl(), getProvider()));
        parameters.add("grant_type", "authorization_code");

        Map<String, ?> oauthResponse =
                new RestTemplate().postForObject(
                        getStep2Url(),
                        parameters,
                        Map.class);

        return getOAuthId(oauthResponse);
    }


    protected String encode(String param) {
        try {
            return URLEncoder.encode(param, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String generateCsrfToken() {
        byte[] bytes = new byte[15];
        secureRandom.nextBytes(bytes);
        return BaseEncoding.base16().encode(bytes);
    }
}
