package org.mixit.cesar.web.app;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.Tuple;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.account.AccountService;
import org.mixit.cesar.service.account.ExpiredTokenException;
import org.mixit.cesar.service.account.InvalidTokenException;
import org.mixit.cesar.service.authentification.AuthenticationInterceptor;
import org.mixit.cesar.service.authentification.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private AccountService accountService;

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    /**
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "/{login}")
    @ResponseStatus(HttpStatus.OK)
    public Tuple user(@PathVariable(value = "login") String login) {
        Account account = accountRepository.findByLogin(login);
        return new Tuple().setKey("login").setValue(account == null ? null : account.getLogin());
    }


    /**
     * Create a new account
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(method = RequestMethod.POST)
    public Credentials user(@RequestBody Account account) {
        return accountService.createNormalAccount(account);
    }


    /**
     * Validates an user account and unlock account
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "/valid")
    public void finalizeCreation(@RequestParam String token, HttpServletResponse response) throws IOException {
        try {
            accountService.validateAccountAfterMailReception(token);
            response.sendRedirect(urlFactory.getBaseUrl() + "/");
        }
        catch (InvalidTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/error/INVALID_TOKEN");
        }
        catch (ExpiredTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/error/EXPIRED_TOKEN");
        }
    }

    /**
     * Send an email to reinit password
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "/password", method = RequestMethod.DELETE)
    public void sendMailForPasswordReinit(@RequestParam String email) {
        accountService.startReinitPassword(email);
    }


    /**
     * Redirect user on password reinit page if token is valid. We need to authenticate the user to do that
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "/password")
    public void passwordReinit(@RequestParam String token) {

    }

}
