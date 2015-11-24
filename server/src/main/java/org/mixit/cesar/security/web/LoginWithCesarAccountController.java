package org.mixit.cesar.security.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.site.model.FlatView;
import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.authentification.CookieService;
import org.mixit.cesar.security.service.authentification.CryptoService;
import org.mixit.cesar.security.service.exception.AccountMustBeConfirmedException;
import org.mixit.cesar.security.service.exception.BadCredentialsException;
import org.mixit.cesar.security.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller used to authenticate users
 */
@Controller
@Transactional
@RequestMapping("/app")
public class LoginWithCesarAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private CryptoService cryptoService;

    /**
     * Authenticates the user and returns the user token which has to be sent in the header of every request
     *
     * @see org.mixit.cesar.security.service.authentification.AuthenticationFilter
     */
    @RequestMapping(value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(FlatView.class)
    public ResponseEntity<Account> authenticate(HttpServletRequest request, HttpServletResponse response) {
        String[] username = request.getParameterValues("username");
        String[] password = request.getParameterValues("password");

        if (username == null || password == null) {
            throw new IllegalArgumentException("User and password are required");
        }

        Account account = accountRepository.findByLogin(username[0]);

        if (account == null) {
            throw new UserNotFoundException();
        }
        else if (!account.getPassword().equals(cryptoService.passwordHash(password[0]))) {
            throw new BadCredentialsException();
        }
        else if (!account.isValid()) {
            throw new AccountMustBeConfirmedException();
        }

        cookieService.setCookieInResponse(response, account);
        return new ResponseEntity<>(account.prepareForView(), HttpStatus.OK);
    }

}
