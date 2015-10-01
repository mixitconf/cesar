package org.mixit.cesar.security.authentification;

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

    public final static String TOKEN_REQUEST_HEADER_PARAM = "X-XSRF-TOKEN";
    public final static String TOKEN_COOKIE_NAME = "XSRF-TOKEN";

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(TOKEN_REQUEST_HEADER_PARAM);

        if (token != null) {
            Account account = accountRepository.findByToken(token);
            if (account != null) {
                currentUser.setCredentials(Credentials.build(account));
                return true;
            }
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials");
        currentUser.setCredentials(null);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
