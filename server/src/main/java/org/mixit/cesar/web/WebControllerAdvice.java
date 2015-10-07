package org.mixit.cesar.web;

import static org.mixit.cesar.model.FunctionalError.Type.*;

import org.mixit.cesar.model.FunctionalError;
import org.mixit.cesar.security.authentification.BadCredentialsException;
import org.mixit.cesar.security.authentification.UserNotFoundException;
import org.mixit.cesar.security.autorisation.AuthenticationRequiredException;
import org.mixit.cesar.security.autorisation.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Definition of the exception handlers
 */
@ControllerAdvice
public class WebControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);

    private ResponseEntity<FunctionalError> buildFunctionalError(Throwable e, FunctionalError.Type type, HttpStatus status){
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
