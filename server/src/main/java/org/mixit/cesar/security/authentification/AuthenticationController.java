package org.mixit.cesar.security.authentification;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.Authority;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.model.session.SessionLanguage;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.repository.AuthorityRepository;
import org.mixit.cesar.security.autorisation.AuthenticationRequiredException;
import org.mixit.cesar.security.oauth.OAuthFactory;
import org.mixit.cesar.web.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller used to authenticate users
 */
@Controller
@Transactional
@RequestMapping("/app")
public class AuthenticationController {

    private AccountRepository accountRepository;
    private AuthorityRepository authorityRepository;
    private OAuthFactory oauthFactory;
    private ApplicationContext applicationContext;
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    public AuthenticationController(
            AccountRepository accountRepository,
            AuthorityRepository authorityRepository,
            OAuthFactory oauthFactory,
            ApplicationContext applicationContext,
            AbsoluteUrlFactory urlFactory) {
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
        this.oauthFactory = oauthFactory;
        this.applicationContext = applicationContext;
        this.urlFactory = urlFactory;
    }

    /**
     * Starts the OAuth dance or authenticate user if he has a standard account
     */
    @RequestMapping(value = "/login-with/{provider}", method = RequestMethod.GET)
    public String startOAuthDance(@PathVariable("provider") String providerName, HttpServletRequest request) throws IOException {
        OAuthProvider provider = toProvider(providerName);
        return "redirect:" + oauthFactory.create(provider).getRedirectUrl(request);
    }

    /**
     * Receives the OAuth callback and authenticates, or signs up, the user
     */
    @RequestMapping(value = "/oauth/{provider}", method = RequestMethod.GET)
    public String oauthCallback(@PathVariable("provider") String providerName,
                                HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        OAuthProvider provider = toProvider(providerName);
        Optional<String> oauthId = oauthFactory.create(provider).getOAuthId(request);
        boolean newAccount= false;

        if (oauthId.isPresent()) {
            Account account = accountRepository.findByOauthProviderAndId(provider, oauthId.get());
            if (account == null) {
                account = accountRepository.save(
                        new Account()
                                .setProvider(provider)
                                .setOauthId(oauthId.get())
                                .setRegisteredAt(LocalDateTime.now())
                                .setDefaultLanguage(SessionLanguage.fr)
                                .setValid(false)
                );
                account.addAuthority(authorityRepository.findByName(Role.MEMBER));
                accountRepository.save(account);
                newAccount = true;
            }

            setCookieInResponse(response, account);
            if(newAccount){
                return String.format("redirect:%s/createuseraccount", urlFactory.getBaseUrl());
            }
            else{
                return String.format("redirect:%s/account", urlFactory.getBaseUrl());
            }
        }
        else {
            response.sendError(500, String.format("Error when you try to authenticate via %s. Try again", providerName));
            return String.format("redirect:%s/error", urlFactory.getBaseUrl());
        }
    }

    /**
     * Receives the OAuth callback and authenticates, or signs up, the user
     */
    @RequestMapping(value = "/login-finalize", method = RequestMethod.GET)
    public Credentials oauthCallback() {
        return applicationContext.getBean(CurrentUser.class).getCredentials().orElse(new Credentials());
    }

    /**
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Credentials> authenticate(HttpServletRequest request, HttpServletResponse response) {
        String[] username = request.getParameterValues("username");
        String[] password = request.getParameterValues("password");

        if (username == null || password == null) {
            throw new IllegalArgumentException("User and password are required");
        }

        Account account = accountRepository.findByLogin(username[0]);

        if (account == null){
            throw new UserNotFoundException();
        }
        else if(!account.getPassword().equals(password[0])) {
            throw new BadCredentialsException();
        }

        return new ResponseEntity<>(setCookieInResponse(response, account), HttpStatus.OK);
    }

    /**
     * Create token if it does'nt exist
     */
    private Credentials setCookieInResponse(HttpServletResponse response, Account account) {
        if (account.getToken() == null) {
            account.setToken(UUID.randomUUID().toString());
            accountRepository.save(account);
        }

        Cookie cookie = new Cookie(AuthenticationInterceptor.TOKEN_COOKIE_NAME, account.getToken());
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).getSeconds());
        response.addCookie(cookie);
        return Credentials.build(account);
    }

    private OAuthProvider toProvider(String pathVariable) {
        return OAuthProvider.valueOf(pathVariable.toUpperCase());
    }

}
