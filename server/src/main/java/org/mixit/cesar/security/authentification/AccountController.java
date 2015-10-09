package org.mixit.cesar.security.authentification;

import org.mixit.cesar.model.Tuple;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Credentials user(@RequestBody Account account) {
        //TODO use a service maybe

        //Step1: we check the required values

        //Step2: we check if login exist

        //Step3: we check if a member exist with the same email

        //Step4: a member is created but invalid

        //Step5: a mail with a token is send to the user. He has to confirm it before 24h

        return null;
    }


    /**
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see AuthenticationInterceptor
     */
    @RequestMapping(value = "create-finalize", method = RequestMethod.GET)
    public Credentials finalizeCreation(@RequestBody Account account) {
        //TODO use a service maybe

        //Step1: we check the token and its validity

        //Step2: the account is now valid and the user can connect to mix-it

        return null;
    }
}
