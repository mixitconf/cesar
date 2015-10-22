package org.mixit.cesar.web;

import static org.mixit.cesar.model.FunctionalError.Type.*;

import org.mixit.cesar.model.FunctionalError;
import org.mixit.cesar.service.authentification.AccountMustBeConfirmedException;
import org.mixit.cesar.service.authentification.BadCredentialsException;
import org.mixit.cesar.service.authentification.UserNotFoundException;
import org.mixit.cesar.service.autorisation.AuthenticationRequiredException;
import org.mixit.cesar.service.autorisation.ForbiddenException;
import org.mixit.cesar.service.user.EmailExistException;
import org.mixit.cesar.service.user.ExpiredTokenException;
import org.mixit.cesar.service.user.InvalidTokenException;
import org.mixit.cesar.service.user.LoginExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private ResponseEntity<FunctionalError> buildFunctionalError(Throwable e, FunctionalError.Type type, HttpStatus status) {
        return new ResponseEntity<>(new FunctionalError().setType(type).setMessage(e.getMessage()), status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<FunctionalError> handleException(BadCredentialsException exception) {
        logger.error("BadCredentialsException ", exception);
        return buildFunctionalError(exception, BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<FunctionalError> handleException(UserNotFoundException exception) {
        logger.error("UserNotFoundException ", exception);
        return buildFunctionalError(exception, USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<FunctionalError> handleException(EmailExistException exception) {
        logger.error("EmailExistException ", exception);
        return buildFunctionalError(exception, EMAIL_EXIST, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<FunctionalError> handleException(ExpiredTokenException exception) {
        logger.error("ExpiredTokenException ", exception);
        return buildFunctionalError(exception, EXPIRED_TOKEN, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<FunctionalError> handleException(InvalidTokenException exception) {
        logger.error("InvalidTokenException ", exception);
        return buildFunctionalError(exception, INVALID_TOKEN, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginExistException.class)
    public ResponseEntity<FunctionalError> handleException(LoginExistException exception) {
        logger.error("LoginExistException ", exception);
        return buildFunctionalError(exception, USER_EXIST, HttpStatus.BAD_REQUEST);
    }

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
