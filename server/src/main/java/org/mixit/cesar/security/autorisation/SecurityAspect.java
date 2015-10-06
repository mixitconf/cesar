package org.mixit.cesar.security.autorisation;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mixit.cesar.security.authentification.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SecurityAspect {
    @Autowired
    private CurrentUser currentUser;

    @Before("@annotation(authenticated)")
    public void checkAccess(JoinPoint joinPoint, Authenticated authenticated) {
        if (!currentUser.getCredentials().isPresent()) {
            throw new AuthenticationRequiredException();
        }
    }

    @Before("@annotation(needsRole)")
    public void checkAccess(JoinPoint joinPoint, NeedsRole needsRole) {
        if (!currentUser.getCredentials().isPresent()) {
            throw new AuthenticationRequiredException();
        }
        List<String> userRoles = currentUser.getCredentials().get().getRoles();
        if (Arrays.stream(needsRole.value()).noneMatch(userRoles::contains)) {
            throw new ForbiddenException();
        }
    }
}
