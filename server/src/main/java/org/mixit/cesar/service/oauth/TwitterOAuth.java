package org.mixit.cesar.service.oauth;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mixit.cesar.model.security.BadRequestException;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of Twitter OAuth, using Scribe, based on
 * <a href="https://github.com/fernandezpablo85/scribe-java/blob/master/src/test/java/org/scribe/examples/TwitterExample.java">this example</a>.
 * @author JB Nizet
 */
@Component
public class TwitterOAuth implements OAuth {

    private static final String TWITTER_TOKEN_ATTRIBUTE = TwitterOAuth.class.getName() + "-token";

    @Value("${oauth.twitter.apiKey}")
    private String apiKey;

    @Value("${oauth.twitter.clientSecret}")
    private String clientSecret;

    @Autowired
    private AbsoluteUrlFactory absoluteUrlFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.TWITTER;
    }

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        OAuthService service = createTwitterService(request);

        Token requestToken = service.getRequestToken();
        request.getSession().setAttribute(TWITTER_TOKEN_ATTRIBUTE, requestToken);
        return service.getAuthorizationUrl(requestToken);
    }

    private OAuthService createTwitterService(HttpServletRequest request) {
        return new ServiceBuilder()
            .provider(TwitterApi.Authenticate.class)
            .apiKey(apiKey)
            .apiSecret(clientSecret)
            .callback(OAuthUtil.getRedirectUrl(absoluteUrlFactory.getBaseUrl(), OAuthProvider.TWITTER))
            .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<String> getOAuthId(HttpServletRequest request) throws BadRequestException, IOException {
        String token = request.getParameter("oauth_token");
        String verifier = request.getParameter("oauth_verifier");

        if (token == null || verifier == null) {
            return Optional.empty();
        }

        Token requestToken = (Token) request.getSession().getAttribute(TWITTER_TOKEN_ATTRIBUTE);
        if (requestToken == null || !token.equals(requestToken.getToken())) {
            throw new BadRequestException("invalid token");
        }

        Verifier v = new Verifier(verifier);
        OAuthService service = createTwitterService(request);
        Token accessToken = service.getAccessToken(requestToken, v);

        OAuthRequest oauthRequest =
            new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
        service.signRequest(accessToken, oauthRequest);
        Response jsonResponse = oauthRequest.send();

        Map<String, ?> json = objectMapper.readValue(jsonResponse.getBody(), Map.class);
        String twitterId = (String) json.get("id_str");
        return Optional.of(twitterId);
    }
}
