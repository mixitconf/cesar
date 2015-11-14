package org.mixit.cesar.model.security;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.mixit.cesar.utils.HashUtil;

/**
 * This element is not serialized in database.
 */
public class Credentials implements Serializable{

    private String oauthId;
    private String login;
    private String token;
    private String firstname;
    private String lastname;
    private String email;
    private String hash;
    private String language;
    private List<String> roles;

    public static Credentials build(Account account) {
        Credentials credentials = new Credentials()
                .setOauthId(account.getOauthId())
                .setToken(account.getToken())
                .setEmail(account.getEmail())
                .setLogin(account.getLogin())
                .setLanguage(account.getDefaultLanguage()!=null ? account.getDefaultLanguage().name() : "fr");

        if(account.getEmail()!=null){
            credentials.setHash(HashUtil.md5Hex(account.getEmail()));
        }
        if (account.getAuthorities() != null) {
            credentials.setRoles(account.getAuthorities().stream().map(a -> a.getName().toString()).collect(Collectors.toList()));
        }
        if (account.getMember() != null) {
            credentials.setLastname(account.getMember().getLastname())
                    .setFirstname(account.getMember().getFirstname())
                    .setHash(HashUtil.md5Hex(account.getMember().getEmail()));
        } else {
            credentials.setLastname(account.getLastname()).setFirstname(account.getFirstname());
        }
        return credentials;
    }

    public String getOauthId() {
        return oauthId;
    }

    public Credentials setOauthId(String oauthId) {
        this.oauthId = oauthId;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public Credentials setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public Credentials setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Credentials setToken(String token) {
        this.token = token;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public Credentials setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public Credentials setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Credentials setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Credentials setLanguage(String language) {
        this.language = language;
        return this;
    }
}
