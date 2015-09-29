package org.mixit.cesar.security.authentification;

/**
 * @author GET <guillaume@dev-mind.fr>
 */
public class Credentials {
    private String login;
    private String token;
    private String name;
    private String email;

    public static Credentials build(CurrentUser user) {
        return new Credentials()
                .setName(user.getName())
                .setToken(user.getToken())
                .setEmail(user.getEmail())
                .setLogin(user.getLogin());
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

    public String getName() {
        return name;
    }

    public Credentials setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Credentials setEmail(String email) {
        this.email = email;
        return this;
    }
}
