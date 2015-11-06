package org.mixit.cesar.web;

import static org.mixit.cesar.model.FunctionalError.Type.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mixit.cesar.model.FunctionalError;
import org.mixit.cesar.model.security.Account;
import org.mixit.cesar.model.security.OAuthProvider;
import org.mixit.cesar.service.AbsoluteUrlFactory;
import org.mixit.cesar.service.account.EmailExistException;
import org.mixit.cesar.service.account.LoginExistException;
import org.mixit.cesar.service.authentification.AccountMustBeConfirmedException;
import org.mixit.cesar.service.authentification.BadCredentialsException;
import org.mixit.cesar.service.authentification.UserNotFoundException;
import org.mixit.cesar.service.autorisation.AuthenticationRequiredException;
import org.mixit.cesar.service.autorisation.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Definition of the exception handlers
 */
@ControllerAdvice
public class WebControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);

    @Autowired
    private AbsoluteUrlFactory urlFactory;

    /**
     * Functionnal error is used to send feedback to user when he uses a screen
     */
    private ResponseEntity<FunctionalError> buildFunctionalError(Throwable e, FunctionalError.Type type, HttpStatus status) {
        return new ResponseEntity<>(new FunctionalError().setType(type).setMessage(e.getMessage()), status);
    }

    /**
     * Error launch if login is incorrect {@link org.mixit.cesar.web.app.AuthenticationController#authenticate(HttpServletRequest, HttpServletResponse)}
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<FunctionalError> handleException(BadCredentialsException exception) {
        logger.error("BadCredentialsException ", exception);
        return buildFunctionalError(exception, BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error launch if user is not found on login {@link org.mixit.cesar.web.app.AuthenticationController#authenticate(HttpServletRequest, HttpServletResponse)}
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<FunctionalError> handleException(UserNotFoundException exception) {
        logger.error("UserNotFoundException ", exception);
        return buildFunctionalError(exception, USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error launch if email already exists {@link org.mixit.cesar.service.account.AccountService#createNormalAccount(Account)} or
     * {@link org.mixit.cesar.service.account.AccountService#updateSocialAccount(OAuthProvider, Account)}
     */
    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<FunctionalError> handleException(EmailExistException exception) {
        logger.error("EmailExistException ", exception);
        return buildFunctionalError(exception, EMAIL_EXIST, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error launch if login already exists {@link org.mixit.cesar.service.account.AccountService#createNormalAccount(Account)}
     */
    @ExceptionHandler(LoginExistException.class)
    public ResponseEntity<FunctionalError> handleException(LoginExistException exception) {
        logger.error("LoginExistException ", exception);
        return buildFunctionalError(exception, USER_EXIST, HttpStatus.BAD_REQUEST);
    }

    /**
     * Error launch when user has to validate his email {@link org.mixit.cesar.web.app.AuthenticationController#authenticate(HttpServletRequest, HttpServletResponse)}
     */
    @ExceptionHandler(AccountMustBeConfirmedException.class)
    public ResponseEntity<FunctionalError> handleException(AccountMustBeConfirmedException exception) {
        logger.error("AccountMustBeConfirmedException ", exception);
        return buildFunctionalError(exception, NEED_VALIDATION, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FunctionalError> handleException(IllegalArgumentException exception) {
        logger.error("IllegalArgumentException ", exception);
        return buildFunctionalError(exception, REQUIRED_ARGS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    public ResponseEntity<FunctionalError> handleException(AuthenticationRequiredException exception) {
        logger.error("AuthenticationRequiredException ", exception);
        return buildFunctionalError(exception, UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<FunctionalError> handleException(ForbiddenException exception) {
        logger.error("ForbiddenException ", exception);
        return buildFunctionalError(exception, FORBIDDEN, HttpStatus.FORBIDDEN);
    }
}
