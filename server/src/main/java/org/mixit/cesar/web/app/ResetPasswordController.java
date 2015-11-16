package org.mixit.cesar.web.app;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.account.ResetPasswordService;
import org.mixit.cesar.service.account.TokenService;
import org.mixit.cesar.service.authentification.CookieService;
import org.mixit.cesar.service.authentification.CryptoService;
import org.mixit.cesar.service.authentification.CurrentUser;
import org.mixit.cesar.service.autorisation.Authenticated;
import org.mixit.cesar.service.exception.AuthenticationRequiredException;
import org.mixit.cesar.service.exception.BadCredentialsException;
import org.mixit.cesar.service.exception.ExpiredTokenException;
import org.mixit.cesar.service.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to authenticate users
 */
@RestController
@RequestMapping("/app/account/password")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private CryptoService cryptoService;

    /**
     * Send an email to reinit password
     *
     * @see org.mixit.cesar.service.authentification.AuthenticationFilter
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public void sendMailForPasswordReinit(@RequestParam(value = "email") String email) {
        resetPasswordService.sendMailToResetPassword(email);
    }


    /**
     * Redirect user on password reinit page if token is valid. We need to authenticate the user to do that
     *
     * @see org.mixit.cesar.service.authentification.AuthenticationFilter
     */
    @RequestMapping
    public void passwordReinit(@RequestParam String token, HttpServletResponse response) throws IOException {
        try {
            Account account = tokenService.getCredentialsForToken(token);
            cookieService.setCookieInResponse(response, account);
            response.sendRedirect(urlFactory.getBaseUrl() + "/passwordreinit");
        }
        catch (InvalidTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/cerror/INVALID_TOKEN");
        }
        catch (ExpiredTokenException e) {
            response.sendRedirect(urlFactory.getBaseUrl() + "/cerror/EXPIRED_TOKEN");
        }
    }

    /**
     * Redirect user on password reinit page if token is valid. We need to authenticate the user to do that
     *
     * @see org.mixit.cesar.service.authentification.AuthenticationFilter
     */
    @RequestMapping(value = "/check")
    @Authenticated
    public boolean passwordReinit(@RequestParam String password) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);

        //If no current user we want an authentication
        currentUser.getCredentials().orElseThrow(AuthenticationRequiredException::new);

        Account account = accountRepository.findByToken(currentUser.getCredentials().get().getToken());
        if (!account.getPassword().equals(cryptoService.passwordHash(password))) {
            throw new BadCredentialsException();
        }
        return true;
    }

}
