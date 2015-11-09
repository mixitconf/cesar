package org.mixit.cesar.web.app;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.account.CreateSocialAccountService;
import org.mixit.cesar.service.authentification.CookieService;
import org.mixit.cesar.service.authentification.RequestedPath;
import org.mixit.cesar.service.oauth.OAuthFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller used to authenticate users
 */
@Controller
@Transactional
@RequestMapping("/app")
public class LoginWithSocialAccountController {

    @Autowired
    private RequestedPath requestedPath;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OAuthFactory oauthFactory;

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    private CreateSocialAccountService createSocialAccountService;

    @Autowired
    private CookieService cookieService;

    /**
     * Starts the OAuth dance or authenticate user if he has a standard account
     */
    @RequestMapping(value = "/login-with/{provider}", method = RequestMethod.GET)
    public String startOAuthDance(
            @PathVariable("provider") String providerName,
            @RequestParam(value = "to", defaultValue = "/") String redirect,
            HttpServletRequest request) throws IOException {

        requestedPath.setValue(redirect);
        OAuthProvider provider = toProvider(providerName);
        return "redirect:" + oauthFactory.create(provider).getRedirectUrl(request);
    }

    /**
     * Receives the OAuth callback and authenticates, or signs up, the user
     */
    @RequestMapping(value = "/oauth/{provider}", method = RequestMethod.GET)
    public void oauthCallback(@PathVariable("provider") String providerName,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        OAuthProvider provider = toProvider(providerName);
        Optional<String> oauthId = oauthFactory.create(provider).getOAuthId(request);
        boolean newAccount = false;

        if (oauthId.isPresent()) {
            Account account = accountRepository.findByOauthProviderAndId(provider, oauthId.get());
            if (account == null) {
                account = createSocialAccountService.createAccount(provider, oauthId.get());
                newAccount = true;
            }

            cookieService.setCookieInResponse(response, account);
            if (newAccount || account.getMember() == null) {
                response.sendRedirect(urlFactory.getBaseUrl() + "/createaccountsocial");
            }
            else {
                response.sendRedirect(urlFactory.getBaseUrl() + requestedPath.getValue());
            }
        }
        else {
            response.sendRedirect(urlFactory.getBaseUrl() + "/cerror/LOGIN_" + providerName);
        }
    }

    private OAuthProvider toProvider(String pathVariable) {
        return OAuthProvider.valueOf(pathVariable.toUpperCase());
    }

}
