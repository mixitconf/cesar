package org.mixit.cesar.security.authentification;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * This interceptor is added in the Spring filters chain, to check if the user token is in the request. If this
 * token is present the current user is loaded. If the token is not in request or if the user doesn't exist a 401 error is send
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    public final static String TOKEN_REQUEST_HEADER = "Cesar-Token";
    public final static String TOKEN_COOKIE = "cesarTokenCookie";
    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN_REQUEST_HEADER);
        if (token != null) {
            Account account = accountRepository.findByToken(token);
            if (account != null) {
                currentUser
                        .setEmail(account.getEmail())
                        .setLogin(account.getLogin())
                        .setName(account.getName())
                        .setToken(account.getToken())
                        .setRoles(account.getAuthorities().stream().map(a -> a.getName().toString()).collect(Collectors.toList()));
                return true;
            }
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
        currentUser.clear();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
