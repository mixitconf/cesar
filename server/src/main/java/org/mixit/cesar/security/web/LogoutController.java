package org.mixit.cesar.security.web;

import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.security.model.Account;
import org.mixit.cesar.security.repository.AccountRepository;
import org.mixit.cesar.security.service.authentification.CookieService;
import org.mixit.cesar.security.service.authentification.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
@RequestMapping("/app/logout")
public class LogoutController {

    @Autowired
    private CookieService cookieService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping
    public void logout(HttpServletResponse response) {
        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        currentUser.getCredentials().ifPresent(credentials -> {
                    Account account = accountRepository.findByToken(credentials.getToken());
                    account.setToken(null);

                }
        );

        cookieService.deleteCookieInResponse(response);
    }

}
