package org.mixit.cesar.web.app;

import org.mixit.cesar.model.Tuple;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.authentification.AuthenticationInterceptor;
import org.mixit.cesar.service.authentification.Credentials;
import org.mixit.cesar.service.user.AccountService;
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

    /**
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "check/{login}")
    @ResponseStatus(HttpStatus.OK)
    public Tuple user(@PathVariable(value = "login") String login) {
        Account account = accountRepository.findByLogin(login);
        return new Tuple().setKey("login").setValue(account == null ? null : account.getLogin());
    }


    /**
     * Authenticates the user
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Credentials user(@RequestBody Account account) {
        return accountService.createNormalAccount(account);
    }


    /**
     * Validates an user account and unlock account
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "valid", method = RequestMethod.GET)
    public Credentials finalizeCreation(@RequestParam String token) {
        return accountService.validateAccountAfterMailReception(token);
    }
}
