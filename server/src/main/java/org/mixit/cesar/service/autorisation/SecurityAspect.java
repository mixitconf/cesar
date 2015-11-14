package org.mixit.cesar.service.autorisation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mixit.cesar.model.security.Role;
import org.mixit.cesar.service.authentification.CurrentUser;
import org.mixit.cesar.service.exception.AuthenticationRequiredException;
import org.mixit.cesar.service.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityAspect {
    @Autowired
    private ApplicationContext applicationContext;

    @Before("@annotation(authenticated)")
    public void checkAccess(JoinPoint joinPoint, Authenticated authenticated) {
        if (!applicationContext.getBean(CurrentUser.class).getCredentials().isPresent()) {
            throw new AuthenticationRequiredException();
        }
    }

    @Before("@annotation(needsRole)")
    public void checkAccess(JoinPoint joinPoint, NeedsRole needsRole) {

        CurrentUser currentUser = applicationContext.getBean(CurrentUser.class);
        if (!currentUser.getCredentials().isPresent()) {
            throw new AuthenticationRequiredException();
        }
        List<Role> userRoles = currentUser.getCredentials().get().getRoles().stream().map(r -> Role.valueOf(r)).collect(Collectors.toList());
        if (Arrays.stream(needsRole.value()).noneMatch(userRoles::contains)) {
            throw new ForbiddenException();
        }
    }
}
