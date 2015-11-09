package org.mixit.cesar.web.app;

import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.authentification.CookieService;
import org.mixit.cesar.service.authentification.Credentials;
import org.mixit.cesar.service.authentification.CurrentUser;
import org.mixit.cesar.service.exception.AuthenticationRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Transactional
@RequestMapping("/app/login-required")
public class CheckAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CookieService cookieService;

    /**
     * When user want to access to a secure page we see if he is already connected on backend
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Credentials> oauthCallback(HttpServletResponse response) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);

        //If no current user we want an authentication
        currentUser.getCredentials().orElseThrow(AuthenticationRequiredException::new);

        Account account = accountRepository.findByToken(currentUser.getCredentials().get().getToken());

        if (account == null) {
            throw new AuthenticationRequiredException();
        }
        else {
            cookieService.setCookieInResponse(response, account);
        }
        return new ResponseEntity<>(cookieService.setCookieInResponse(response, account), HttpStatus.OK);
    }

}
