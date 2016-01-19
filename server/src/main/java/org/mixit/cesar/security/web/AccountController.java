package org.mixit.cesar.security.web;

import static org.mixit.cesar.site.config.CesarCacheConfig.CACHE_MEMBER;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.model.Role;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.account.CreateCesarAccountService;
import org.mixit.cesar.security.service.account.CreateSocialAccountService;
import org.mixit.cesar.security.service.account.TokenService;
import org.mixit.cesar.security.service.authentification.CookieService;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.mixit.cesar.security.service.autorisation.Authenticated;
import org.mixit.cesar.security.service.autorisation.NeedsRole;
import org.mixit.cesar.security.service.exception.AuthenticationRequiredException;
import org.mixit.cesar.security.service.exception.ExpiredTokenException;
import org.mixit.cesar.security.service.exception.InvalidTokenException;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.site.model.Tuple;
import org.mixit.cesar.site.model.UserView;
import org.mixit.cesar.site.service.AbsoluteUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to authenticate users
 */
@RestController
@RequestMapping("/app/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreateCesarAccountService createCesarAccountService;

    @Autowired
    private CreateSocialAccountService createSocialAccountService;

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * When we create a new user we want to know if a login is already used. This method checks the login
     */
    @RequestMapping(value = "/cesar/{login}")
    @ResponseStatus(HttpStatus.OK)
    public Tuple user(@PathVariable(value = "login") String login) {
        Account account = accountRepository.findByLogin(login);
        return new Tuple().setKey("login").setValue(account == null ? null : account.getLogin());
    }

    /**
     * A user can see his own informations
     */
    @RequestMapping(value = "/{oauthid}")
    @NeedsRole(Role.MEMBER)
    @JsonView(FlatView.class)
    @Authenticated
    public ResponseEntity<Account> find(@PathVariable(value = "oauthid") String oauthid) {

        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        if (oauthid.equals(currentUser.getCredentials().get().getOauthId())) {
            return new ResponseEntity<>(currentUser.getCredentials().get(), HttpStatus.OK);
        }
        throw new AuthenticationRequiredException();
    }

    /**
     * User can update his own informations
     */
    @RequestMapping(method = RequestMethod.PUT)
    @JsonView(FlatView.class)
    public ResponseEntity<Account> updateUser(@RequestBody Account account) {
        Account accountSaved = createCesarAccountService.updateAccount(account);
        cacheManager.getCache(CACHE_MEMBER).clear();
        return new ResponseEntity<>(accountSaved, HttpStatus.OK);
    }

    /**
     * Creates a new Cesar account when the user don't want to use a social network to manage his authentication
     */
    @RequestMapping(value = "/cesar", method = RequestMethod.POST)
    @JsonView(FlatView.class)
    public ResponseEntity<Account> user(@RequestBody Account account) {
        Account accountSaved = createCesarAccountService.createNormalAccount(account);
        cacheManager.getCache(CACHE_MEMBER).clear();
        return new ResponseEntity<>(accountSaved, HttpStatus.OK);
    }

    /**
     * When a person uses a social network to connect on the website, he has to define his email to complete the account created on the webSite.
     * The controller calls this method to update his data
     */
    @RequestMapping(value = "/social", method = RequestMethod.PUT)
    public ResponseEntity updateSocialAccountAfterCreation(@RequestBody Account account) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);

        //The user must be connected
        currentUser.getCredentials().orElseThrow(AuthenticationRequiredException::new);
        createSocialAccountService.updateAccount(
                account,
                currentUser.getCredentials().get().getToken(),
                currentUser.getCredentials().get().getOauthId());
        cacheManager.getCache(CACHE_MEMBER).clear();

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * When a new account is created we send an email to the future user to validate his email. When he clicks on the link, this method
     * is called. Its aim is to validate the account and unlock it.
     */
    @RequestMapping(value = "/valid")
    public void finalizeCreation(@RequestParam String token, HttpServletResponse response) throws IOException {
        try {
            cookieService.deleteCookieInResponse(response);
            Account account = tokenService.getCredentialsForToken(token);
            cookieService.setCookieInResponse(response, account);
            response.sendRedirect(urlFactory.getBaseUrl() + "/valid");
        }
        catch (InvalidTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/cerror/INVALID_TOKEN");
        }
        catch (ExpiredTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/cerror/EXPIRED_TOKEN");
        }
    }


    /**
     * When user wants to access to a secure page we see if he is already connected on backend
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @JsonView(UserView.class)
    public ResponseEntity<Account> oauthCallback(HttpServletResponse response) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);

        //If no current user we want an authentication
        currentUser.getCredentials().orElseThrow(AuthenticationRequiredException::new);

        Account account = accountRepository.findByToken(currentUser.getCredentials().get().getToken());
        if (account == null) {
            throw new AuthenticationRequiredException();
        }
        cookieService.setCookieInResponse(response, account);
        return new ResponseEntity<>(account.prepareForView(true), HttpStatus.OK);
    }
}
