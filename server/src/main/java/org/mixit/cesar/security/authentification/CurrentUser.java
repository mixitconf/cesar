package org.mixit.cesar.security.authentification;

import java.util.List;

import org.mixit.cesar.model.security.Account;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Current user bean sored in the request scope
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {
    
    private String login;
    private String token;
    private String name;
    private String email;

    public CurrentUser clear(){
        setLogin(null);
        setToken(null);
        setName(null);
        setEmail(null);
        return this;
    }
    private List<String> roles;

    public String getLogin() {
        return login;
    }

    public CurrentUser setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getToken() {
        return token;
    }

    public CurrentUser setToken(String token) {
        this.token = token;
        return this;
    }

    public String getName() {
        return name;
    }

    public CurrentUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CurrentUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public CurrentUser setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

}