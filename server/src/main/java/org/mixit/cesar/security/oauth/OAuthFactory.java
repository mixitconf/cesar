package org.mixit.cesar.security.oauth;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mixit.cesar.model.security.OAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory for OAuth implementations
 *
 * @author JB Nizet
 */
@Component
public class OAuthFactory {
    private Map<OAuthProvider, OAuth> oauthsByProvider;

    @Autowired
    public OAuthFactory(List<OAuth> oauths) {
        oauthsByProvider = oauths.stream().collect(Collectors.toMap(OAuth::getProvider, Function.<OAuth>identity()));
    }

    public OAuth create(OAuthProvider provider) {
        return Optional.ofNullable(oauthsByProvider.get(provider)).orElseThrow(
                () -> new IllegalArgumentException("unknown OAuth provider: " + provider));
    }
}
