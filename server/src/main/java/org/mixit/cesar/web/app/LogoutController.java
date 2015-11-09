package org.mixit.cesar.web.app;

import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.service.authentification.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Transactional
@RequestMapping("/app/logout")
public class LogoutController {

    @Autowired
    private CookieService cookieService;

    @RequestMapping
    public void logout(HttpServletResponse response) {
        cookieService.deleteCookieInResponse(response);
    }

}
